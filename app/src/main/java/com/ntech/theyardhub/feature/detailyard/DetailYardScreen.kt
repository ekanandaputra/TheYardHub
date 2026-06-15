package com.ntech.theyardhub.feature.detailyard

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.RouteName.CHAT_SCREEN
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.theme.textGray
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.LoadImageWithGlide
import com.ntech.theyardhub.core.utils.toRupiahFormat
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.datalayer.model.OrderModel
import com.ntech.theyardhub.feature.product.ProductItem
import com.ntech.theyardhub.feature.order.OrderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.ntech.theyardhub.datalayer.model.CartItemModel
import com.ntech.theyardhub.feature.cart.CartViewModel
import org.koin.androidx.compose.get
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.net.URLEncoder

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailYardScreen(navController: NavController, yardId: String) {

    val viewModel: DetailYardViewModel = get()
    val cartViewModel: CartViewModel = get()
    val orderViewModel: OrderViewModel = get()
    val products = remember { mutableStateListOf<ProductModel>() }

    var showProductSheet by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<ProductModel?>(null) }
    val mContext = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchYard(yardId)
    }

    val yardState = viewModel.yardLiveData.observeAsState().value
    val chatRoomId = viewModel.chatRoomId.observeAsState().value
    val productState = viewModel.productsLiveData.observeAsState().value

    val showDialog = remember { mutableStateOf(false) }
    var data by remember { mutableStateOf(YardModel()) }

    showDialog.value = yardState is AppResponse.Loading

    if (yardState is AppResponse.Success) {
        data = yardState.data
        LaunchedEffect(data.userDocumentId) {
            if (data.userDocumentId.isNotEmpty()) {
                viewModel.fetchProducts(data.userDocumentId)
            }
        }
    }

    if (productState is AppResponse.Success) {
        products.clear()
        products.addAll(productState.data)
    }

    if (chatRoomId != null) {
        navController.navigate("$CHAT_SCREEN/$chatRoomId")
    }

    val actionState = orderViewModel.actionLiveData.observeAsState().value

    when (actionState) {
        is AppResponse.Loading -> {
            showDialog.value = true
        }
        is AppResponse.Success -> {
            showDialog.value = false
            Toast.makeText(mContext, actionState.data, Toast.LENGTH_SHORT).show()
        }
        is AppResponse.Error -> {
            showDialog.value = false
            Toast.makeText(mContext, actionState.message, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }

    if (showDialog.value) LoadingDialog {}

    if (showProductSheet && (selectedProduct != null)) {
        ProductDetailBottomSheet(
            product = selectedProduct!!,
            farmName = data.name,
            sellerId = data.userDocumentId,
            sellerName = data.ownerName,
            phoneNumber = data.phoneNumber,
            onDismiss = {
                showProductSheet = false
            },
            onAddToCartClicked = { cartItem ->
                cartViewModel.addToCart(cartItem)
                showProductSheet = false
                navController.navigate(com.ntech.theyardhub.core.RouteName.CART_SCREEN)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        data.name,
                        style = Typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            LoadImageWithGlide(
                imageUrl = data.thumbnail,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            )

            Column(Modifier.padding(24.dp)) {
                Text(
                    text = data.name,
                    style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = Black)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = data.description,
                    style = Typography.bodyLarge.copy(lineHeight = 24.sp, color = Black.copy(alpha = 0.7f))
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Location Section
                Text(
                    text = "Location",
                    style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = bluePrimary)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    AndroidView(
                        factory = { context ->
                            MapView(context).apply {
                                setTileSource(TileSourceFactory.MAPNIK)
                                setMultiTouchControls(false) // Read-only feel
                                controller.setZoom(15.0)
                                val point = GeoPoint(data.locationModel.latitude, data.locationModel.longitude)
                                controller.setCenter(point)

                                val marker = Marker(this)
                                marker.position = point
                                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                overlays.add(marker)
                            }
                        },
                        update = { mapView ->
                            val point = GeoPoint(data.locationModel.latitude, data.locationModel.longitude)
                            mapView.controller.setCenter(point)
                            val marker = mapView.overlays.filterIsInstance<Marker>().firstOrNull()
                            marker?.position = point
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = bluePrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = data.address.ifEmpty { "No address provided" },
                        style = Typography.bodyMedium.copy(color = textGray)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Products Section
                Text(
                    text = "Featured Products",
                    style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = bluePrimary)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                if (products.isEmpty()) {
                    Text("No products available.", style = Typography.bodyMedium, color = Color.Gray)
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(products) { product ->
                            ProductItem(
                                product = product,
                                onClickItem = {
                                    selectedProduct = it
                                    showProductSheet = true
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(100.dp)) // Padding for bottom bar
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailBottomSheet(
    product: ProductModel,
    farmName: String,
    sellerId: String,
    sellerName: String,
    phoneNumber: String,
    onDismiss: () -> Unit,
    onAddToCartClicked: (CartItemModel) -> Unit
) {
    val context = LocalContext.current
    val cartViewModel: CartViewModel = get()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = White,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Image Carousel
            if (product.images.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(product.images) { imageUrl ->
                        LoadImageWithGlide(
                            imageUrl = imageUrl,
                            contentDescription = "",
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(250.dp)
                        )
                    }
                }
            } else {
                LoadImageWithGlide(
                    imageUrl = product.imageUrl,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }

            Column(Modifier.padding(24.dp)) {
                Text(
                    text = product.name,
                    style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = product.price.toRupiahFormat(),
                    style = Typography.headlineSmall.copy(color = bluePrimary, fontWeight = FontWeight.ExtraBold)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Description",
                    style = Typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product.description,
                    style = Typography.bodyMedium.copy(color = Black.copy(alpha = 0.7f))
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                GeneralButton(
                    onButtonClicked = {
                        val message = "Halo, saya tertarik dengan produk *${product.name}* di farm *$farmName* Anda."
                        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${URLEncoder.encode(message, "UTF-8")}"
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        context.startActivity(intent)
                    },
                    label = "Chat via WhatsApp",
                    buttonType = ButtonType.SECONDARY,
                    buttonHeight = ButtonHeight.MEDIUM,
                    isEnabled = phoneNumber.isNotEmpty()
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                GeneralButton(
                    onButtonClicked = {
                        val cartItem = CartItemModel(
                            userId = cartViewModel.getUserId(),
                            productId = product.documentId,
                            productName = product.name,
                            price = product.price,
                            imageUrl = if (product.images.isNotEmpty()) product.images.first() else product.imageUrl,
                            quantity = 1,
                            sellerId = sellerId,
                            sellerName = sellerName
                        )
                        onAddToCartClicked(cartItem)
                    },
                    label = "Add to Cart",
                    buttonType = ButtonType.PRIMARY,
                    buttonHeight = ButtonHeight.MEDIUM,
                    isEnabled = true
                )
                if (phoneNumber.isEmpty()) {
                    Text(
                        "WhatsApp contact not available",
                        style = Typography.labelSmall.copy(color = Color.Red),
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
