package com.ntech.theyardhub.feature.order

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.theme.Black
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
fun OrderDetailScreen(navController: NavController, orderId: String) {
    val viewModel: OrderViewModel = get()
    val mContext = LocalContext.current
    val orderState by viewModel.orderDetailLiveData.observeAsState()
    val actionState by viewModel.actionLiveData.observeAsState()

    var shippingInput by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(orderId) {
        viewModel.fetchOrderDetail(orderId)
    }

    when (actionState) {
        is AppResponse.Loading -> showDialog.value = true
        is AppResponse.Success -> {
            showDialog.value = false
            Toast.makeText(mContext, "Action Successful", Toast.LENGTH_SHORT).show()
            viewModel.fetchOrderDetail(orderId)
        }
        is AppResponse.Error -> {
            showDialog.value = false
            Toast.makeText(mContext, (actionState as AppResponse.Error).message, Toast.LENGTH_SHORT).show()
        }
        else -> showDialog.value = false
    }

    if (showDialog.value) LoadingDialog {}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Pesanan", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold)) },
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
        when (val state = orderState) {
            is AppResponse.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = bluePrimary)
                }
            }
            is AppResponse.Success -> {
                val order = state.data
                val isSeller = order.sellerId == viewModel.getUserId()
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LoadImageWithGlide(
                            imageUrl = order.productImageUrl,
                            contentDescription = "",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            val translatedStatus = when (order.status) {
                                "PENDING" -> "MENUNGGU"
                                "OFFERED" -> "DITAWARKAN"
                                "ACCEPTED" -> "DITERIMA"
                                "CANCELLED" -> "DIBATALKAN"
                                else -> order.status
                            }
                            val color = when (order.status) {
                                "PENDING" -> Color(0xFFFFA000)
                                "OFFERED" -> bluePrimary
                                "ACCEPTED" -> Color(0xFF388E3C)
                                "CANCELLED" -> Color.Red
                                else -> Color.Gray
                            }
                            Text(order.productName, style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(4.dp))
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
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    DetailInfoRow(label = "Pembeli", value = order.buyerName)
                    DetailInfoRow(label = "Penjual", value = order.sellerName)
                    DetailInfoRow(label = "Alamat Pengiriman", value = order.address.ifEmpty { "-" })
                    DetailInfoRow(label = "Jumlah", value = "${order.quantity} pcs")
                    DetailInfoRow(label = "Harga Dasar", value = order.basePrice.toRupiahFormat())
                    
                    if (order.status != "PENDING") {
                        DetailInfoRow(label = "Biaya Pengiriman", value = order.shippingCost.toRupiahFormat())
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                        DetailInfoRow(label = "Total Harga", value = order.totalPrice.toRupiahFormat(), isBold = true)
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    if (isSeller && (order.status == "PENDING")) {
                        Text("Masukkan Biaya Pengiriman", style = Typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = shippingInput,
                            onValueChange = { shippingInput = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Biaya Pengiriman") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            prefix = { Text("Rp ") },
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        GeneralButton(
                            onButtonClicked = {
                                val cost = shippingInput.toIntOrNull() ?: 0
                                if (cost > 0) {
                                    viewModel.sendOffer(order.orderId, cost, order.basePrice)
                                } else {
                                    Toast.makeText(mContext, "Silakan masukkan biaya pengiriman yang valid", Toast.LENGTH_SHORT).show()
                                }
                            },
                            label = "Kirim Penawaran",
                            buttonType = ButtonType.PRIMARY,
                            buttonHeight = ButtonHeight.MEDIUM,
                            isEnabled = true
                        )
                    }

                    if (!isSeller && order.status == "OFFERED") {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                GeneralButton(
                                    onButtonClicked = { viewModel.updateStatus(order.orderId, "CANCELLED") },
                                    label = "Tolak",
                                    buttonType = ButtonType.SECONDARY,
                                    buttonHeight = ButtonHeight.MEDIUM,
                                    isEnabled = true
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                GeneralButton(
                                    onButtonClicked = { viewModel.updateStatus(order.orderId, "ACCEPTED") },
                                    label = "Terima",
                                    buttonType = ButtonType.PRIMARY,
                                    buttonHeight = ButtonHeight.MEDIUM,
                                    isEnabled = true
                                )
                            }
                        }
                    }
                    
                    if (order.status == "ACCEPTED") {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Pesanan telah diterima. Silakan berkoordinasi untuk pengiriman.",
                                modifier = Modifier.padding(16.dp),
                                style = Typography.bodyMedium.copy(color = Color(0xFF2E7D32), fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
fun DetailInfoRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = Typography.bodyMedium.copy(color = Color.Gray))
        Text(
            value,
            style = Typography.bodyMedium.copy(
                fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Medium,
                color = if (isBold) bluePrimary else Black
            )
        )
    }
}
