package com.ntech.theyardhub.feature.cart

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.text.input.TextFieldValue
import coil.compose.AsyncImage
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.RouteName.ORDER_LIST_SCREEN
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.component.ModernTextField
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.toRupiahFormat
import com.ntech.theyardhub.datalayer.model.CartItemModel
import com.ntech.theyardhub.datalayer.model.OrderModel
import com.ntech.theyardhub.feature.order.OrderViewModel
import org.koin.androidx.compose.get

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController) {
    val viewModel: CartViewModel = get()
    val orderViewModel: OrderViewModel = get()
    val mContext = LocalContext.current

    val cartItemsState by viewModel.cartItemsLiveData.observeAsState()
    val actionState by viewModel.actionLiveData.observeAsState()
    val orderActionState by orderViewModel.actionLiveData.observeAsState()

    var cartItems by remember { mutableStateOf(listOf<CartItemModel>()) }
    var isLoading by remember { mutableStateOf(false) }
    var address by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        viewModel.fetchCartItems()
    }

    when (val state = cartItemsState) {
        is AppResponse.Loading -> isLoading = true
        is AppResponse.Success -> {
            isLoading = false
            cartItems = state.data
        }
        is AppResponse.Error -> {
            isLoading = false
            Toast.makeText(mContext, state.message, Toast.LENGTH_SHORT).show()
        }
        else -> isLoading = false
    }

    when (val state = actionState) {
        is AppResponse.Loading -> isLoading = true
        is AppResponse.Success -> {
            isLoading = false
            Toast.makeText(mContext, state.data, Toast.LENGTH_SHORT).show()
        }
        is AppResponse.Error -> {
            isLoading = false
            Toast.makeText(mContext, state.message, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }

    LaunchedEffect(orderActionState) {
        if (orderActionState is AppResponse.Success) {
            viewModel.clearCart()
            navController.navigate(ORDER_LIST_SCREEN)
        }
    }

    if (isLoading) LoadingDialog {}

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
                            contentDescription = "Kembali"
                        )
                        Text(
                            "Keranjang Saya",
                            modifier = Modifier.padding(start = 8.dp),
                            style = Typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                val totalPrice = cartItems.sumOf { it.price * it.quantity }
                BottomAppBar(
                    modifier = Modifier.height(220.dp),
                    containerColor = White,
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        ModernTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = "Alamat Pengiriman",
                            hint = "Masukkan alamat lengkap Anda"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Harga", style = Typography.bodyLarge)
                            Text(
                                totalPrice.toRupiahFormat(),
                                style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = bluePrimary)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        GeneralButton(
                            onButtonClicked = {
                                if (address.text.isBlank()) {
                                    Toast.makeText(mContext, "Silakan masukkan alamat Anda", Toast.LENGTH_SHORT).show()
                                    return@GeneralButton
                                }
                                cartItems.forEach { item ->
                                    orderViewModel.createOrder(
                                        OrderModel(
                                            buyerId = viewModel.getUserId(),
                                            sellerId = item.sellerId,
                                            buyerName = orderViewModel.getUserName(),
                                            sellerName = item.sellerName,
                                            productName = item.productName,
                                            productId = item.productId,
                                            productImageUrl = item.imageUrl,
                                            quantity = item.quantity,
                                            basePrice = item.price,
                                            totalPrice = item.price * item.quantity,
                                            status = "PENDING",
                                            address = address.text
                                        )
                                    )
                                }
                            },
                            label = "Checkout",
                            buttonType = ButtonType.PRIMARY,
                            buttonHeight = ButtonHeight.MEDIUM,
                            isEnabled = true
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        if (cartItems.isEmpty() && !isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Keranjang Anda kosong", style = Typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cartItems) { item ->
                    CartItem(
                        item = item,
                        onDelete = { viewModel.removeFromCart(item.cartItemId) },
                        onQuantityChange = { viewModel.updateQuantity(item.cartItemId, it) }
                    )
                }
            }
        }
    }
}

@Composable
fun CartItem(
    item: CartItemModel,
    onDelete: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.productName, style = Typography.titleSmall, maxLines = 1)
                Text(item.sellerName, style = Typography.labelSmall, color = Color.Gray)
                Text(
                    item.price.toRupiahFormat(),
                    style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = bluePrimary)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Text("-", style = Typography.titleMedium)
                    }
                    Text(
                        item.quantity.toString(),
                        modifier = Modifier.padding(horizontal = 8.dp),
                        style = Typography.bodyMedium
                    )
                    IconButton(
                        onClick = { onQuantityChange(item.quantity + 1) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Text("+", style = Typography.titleMedium)
                    }
                }
            }
        }
    }
}
