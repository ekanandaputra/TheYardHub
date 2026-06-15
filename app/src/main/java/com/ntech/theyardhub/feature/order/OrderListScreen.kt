package com.ntech.theyardhub.feature.order

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ntech.theyardhub.core.RouteName
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.LoadImageWithGlide
import com.ntech.theyardhub.core.utils.toRupiahFormat
import com.ntech.theyardhub.datalayer.model.OrderModel
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(navController: NavController) {
    val viewModel: OrderViewModel = get()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Pembelian", "Penjualan")

    val orderState by viewModel.orderListLiveData.observeAsState()

    LaunchedEffect(selectedTab) {
        if (selectedTab == 0) {
            viewModel.fetchPurchases()
        } else {
            viewModel.fetchSales()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pesanan Saya", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        containerColor = White
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = White,
                contentColor = bluePrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = bluePrimary
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, style = Typography.labelLarge) }
                    )
                }
            }

            when (val state = orderState) {
                is AppResponse.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = bluePrimary)
                    }
                }
                is AppResponse.Success -> {
                    if (state.data.isEmpty()) {
                        EmptyOrders()
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.data) { order ->
                                OrderItem(order = order, onClick = {
                                    navController.navigate("${RouteName.ORDER_DETAIL_SCREEN}/${order.orderId}")
                                })
                            }
                        }
                    }
                }
                is AppResponse.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${state.message}", color = Color.Red)
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun OrderItem(order: OrderModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            LoadImageWithGlide(
                imageUrl = order.productImageUrl,
                contentDescription = "",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(order.productName, style = Typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text(
                    text = "${order.quantity} pcs",
                    style = Typography.labelSmall.copy(color = Color.Gray)
                )
                Text(
                    text = if (order.status == "PENDING") order.basePrice.toRupiahFormat() else order.totalPrice.toRupiahFormat(),
                    style = Typography.bodyMedium.copy(color = bluePrimary, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                StatusChip(status = order.status)
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val translatedStatus = when (status) {
        "PENDING" -> "MENUNGGU"
        "OFFERED" -> "DITAWARKAN"
        "ACCEPTED" -> "DITERIMA"
        "CANCELLED" -> "DIBATALKAN"
        else -> status
    }
    val color = when (status) {
        "PENDING" -> Color(0xFFFFA000)
        "OFFERED" -> bluePrimary
        "ACCEPTED" -> Color(0xFF388E3C)
        "CANCELLED" -> Color.Red
        else -> Color.Gray
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = translatedStatus,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = Typography.labelSmall.copy(color = color, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun EmptyOrders() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Belum ada pesanan", style = Typography.bodyLarge, color = Color.Gray)
    }
}
