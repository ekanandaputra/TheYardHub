package com.ntech.theyardhub.feature.detailproduct

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.RouteName.CART_SCREEN
import com.ntech.theyardhub.core.RouteName.CHAT_SCREEN
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.LoadImageWithGlide
import com.ntech.theyardhub.core.utils.toRupiahFormat
import com.ntech.theyardhub.datalayer.model.CartItemModel
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.feature.cart.CartViewModel
import org.koin.androidx.compose.get

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProductScreen(navController: NavController, productId: String) {

    val viewModel: DetailProductViewModel = get()
    val cartViewModel: CartViewModel = get()
    val mContext = LocalContext.current

    LaunchedEffect(productId) {
        viewModel.fetchProductDetail(productId)
    }

    val productState by viewModel.productLiveData.observeAsState()
    val cartActionState by cartViewModel.actionLiveData.observeAsState()
    val showDialog = remember { mutableStateOf(false) }
    var product = remember { mutableStateOf(ProductModel()) }

    when (val state = productState) {
        is AppResponse.Loading -> {
            showDialog.value = true
        }

        is AppResponse.Success -> {
            showDialog.value = false
            product.value = state.data
        }

        is AppResponse.Error -> {
            showDialog.value = false
            Toast.makeText(mContext, state.message, Toast.LENGTH_SHORT).show()
        }

        else -> {
            showDialog.value = false
        }
    }

    when (val state = cartActionState) {
        is AppResponse.Loading -> showDialog.value = true
        is AppResponse.Success -> {
            showDialog.value = false
            Toast.makeText(mContext, state.data, Toast.LENGTH_SHORT).show()
            navController.navigate(CART_SCREEN)
        }
        is AppResponse.Error -> {
            showDialog.value = false
            Toast.makeText(mContext, state.message, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }

    if (showDialog.value) LoadingDialog {}

    Scaffold(
        containerColor = White,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                        Text(
                            "Product Detail",
                            modifier = Modifier.padding(start = 8.dp),
                            style = Typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(140.dp),
                containerColor = White,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GeneralButton(
                        onButtonClicked = {
                            val data = product.value
                            cartViewModel.addToCart(
                                CartItemModel(
                                    userId = cartViewModel.getUserId(),
                                    productId = productId,
                                    productName = data.name,
                                    price = data.price,
                                    imageUrl = if (data.images.isNotEmpty()) data.images[0] else data.imageUrl,
                                    quantity = 1,
                                    sellerId = data.userDocumentId,
                                    sellerName = "Owner" // Ideally fetch owner name
                                )
                            )
                        },
                        label = "Add to Cart",
                        buttonType = ButtonType.PRIMARY,
                        buttonHeight = ButtonHeight.MEDIUM,
                        isEnabled = true,
                    )
                    GeneralButton(
                        onButtonClicked = {
                            navController.navigate(CHAT_SCREEN)
                        },
                        label = "Chat with Owner",
                        buttonType = ButtonType.SECONDARY,
                        buttonHeight = ButtonHeight.MEDIUM,
                        isEnabled = true,
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            val productData = product.value
            if (productData.images.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(productData.images.size) { index ->
                        LoadImageWithGlide(
                            imageUrl = productData.images[index],
                            contentDescription = "Product Image",
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(300.dp)
                        )
                    }
                }
            } else if (productData.imageUrl.isNotEmpty()) {
                LoadImageWithGlide(
                    imageUrl = productData.imageUrl,
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }

            Column(Modifier.padding(24.dp)) {
                Text(
                    text = productData.name,
                    style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = productData.price.toRupiahFormat(),
                    style = Typography.titleLarge.copy(color = Color.Black, fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Description",
                    style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = productData.description,
                    style = Typography.bodyLarge,
                    color = Color.Gray
                )
            }
        }
    }
}