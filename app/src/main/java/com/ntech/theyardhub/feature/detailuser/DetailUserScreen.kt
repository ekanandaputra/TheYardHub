package com.ntech.theyardhub.feature.detailuser

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.RouteName
import com.ntech.theyardhub.core.RouteName.LOGIN_SCREEN
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.component.LoginAlert
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.LoadImageWithGlide
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.feature.product.ProductItem
import org.koin.androidx.compose.get

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailUserScreen(navController: NavController) {

    val viewModel: DetailUserViewModel = get()

    LaunchedEffect(Unit) {
        if (!viewModel.getIsGuest()) {
            viewModel.fetchDetailUser()
            viewModel.fetchUserProducts()
        }
    }

    val userState = viewModel.userLiveData.observeAsState().value
    val productState = viewModel.productsLiveData.observeAsState().value

    val showDialog = remember { mutableStateOf(false) }

    var data = UserModel()
    var products = arrayListOf<ProductModel>()

    when (userState) {
        is AppResponse.Loading -> {
            showDialog.value = true
        }

        is AppResponse.Empty -> {
            showDialog.value = false
        }

        is AppResponse.Success -> {
            showDialog.value = false
            data = userState.data
        }

        else -> {
            showDialog.value = false
        }
    }

    when (productState) {
        is AppResponse.Loading -> {
        }

        is AppResponse.Empty -> {
        }

        is AppResponse.Success -> {
            products = productState.data as ArrayList<ProductModel>
        }

        else -> {
        }
    }

    if (showDialog.value) LoadingDialog {}

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Chevron Left"
                        )
                        Text(
                            "Profile",
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
        containerColor = White
    ) { innerPadding ->
        if (viewModel.getIsGuest()) {
            LoginAlert(onButtonClicked = { navController.navigate(LOGIN_SCREEN) })
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        top = innerPadding.calculateTopPadding() + 8.dp,
                        bottom = 16.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadImageWithGlide(
                    imageUrl = "https://images-squarespace--cdn-com.translate.goog/content/v1/552ed2d1e4b0745abca6723d/3e60e68a-5ee9-4f49-9261-e890a6673173/grape+3.jpg?format=2500w&_x_tr_sl=en&_x_tr_tl=id&_x_tr_hl=id&_x_tr_pto=tc",
                    contentDescription = "",
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = data.name,
                    style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    GeneralButton(
                        onButtonClicked = {
                            navController.navigate(RouteName.ORDER_LIST_SCREEN)
                        },
                        label = "My Orders",
                        buttonType = ButtonType.PRIMARY,
                        buttonHeight = ButtonHeight.MEDIUM,
                        isEnabled = true,
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                if (data.yard.name == "") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        GeneralButton(
                            onButtonClicked = {
                                navController.navigate(RouteName.REGISTER_YARD_SCREEN)
                            },
                            label = "Register Farm",
                            buttonType = ButtonType.SECONDARY,
                            buttonHeight = ButtonHeight.MEDIUM,
                            isEnabled = true,
                        )
                    }
                } else {
                    DetailYard(
                        data.yard,
                        onClickEditDetailFarm = { navController.navigate(RouteName.REGISTER_YARD_SCREEN) },
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                DetailUserProduct(
                    products,
                    navController = navController,
                    onClickAddProduct = { navController.navigate(RouteName.CREATE_PRODUCT_SCREEN) })
            }
        }
    }
}

@Composable
fun DetailYard(
    yard: YardModel,
    onClickEditDetailFarm: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(bluePrimary),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Farm",
                    style = Typography.titleSmall.copy(color = White, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Edit Data",
                    style = Typography.labelSmall.copy(color = White),
                    modifier = Modifier.clickable { onClickEditDetailFarm.invoke() },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = yard.name,
                style = Typography.titleMedium.copy(color = White, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = yard.description,
                style = Typography.bodyMedium.copy(color = White)
            )
        }
    }
}

@Composable
fun DetailUserProduct(
    products: ArrayList<ProductModel>,
    navController: NavController,
    onClickAddProduct: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Products",
                style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Add Product",
                style = Typography.labelSmall.copy(color = bluePrimary),
                modifier = Modifier.clickable {
                    onClickAddProduct.invoke()
                })
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (products.isEmpty()) {
            Text(
                text = "No products yet",
                style = Typography.bodyMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(products.size) { item ->
                    ProductItem(
                        product = products[item],
                        onClickItem = {
                            navController.navigate("${RouteName.DETAIL_PRODUCT_SCREEN}/${it.documentId}")
                        }
                    )
                }
            }
        }
    }
}
