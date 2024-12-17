package com.ntech.theyardhub.feature.detailuser

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.core.RouteName
import com.ntech.theyardhub.core.component.LoadingDialog
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

    val coroutineScope = rememberCoroutineScope()
    val viewModel: DetailUserViewModel = get()
    val mContext = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchDetailUser()
        viewModel.fetchUserProducts()
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

    if (showDialog.value) LoadingDialog(setShowDialog = {})

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = "Chevron Left"
                        )
                        Text(
                            "Profile",
                            modifier = Modifier.padding(start = 8.dp),
                            style = Typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = White
                )
            )
        },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding() + 8.dp,
                ),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
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
                Spacer(modifier = Modifier.height(32.dp))
                DetailProfile(
                    data.yard,
                    products,
                    onClickAddProduct = { navController.navigate(RouteName.CREATE_PRODUCT_SCREEN) }
                )
            }

        }
    }
}

@Composable
fun DetailProfile(
    yard: YardModel,
    products: ArrayList<ProductModel>,
    onClickAddProduct: () -> Unit
) {
    Card(
        modifier = Modifier.background(color = White),
        colors = CardDefaults.cardColors(bluePrimary)
    ) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = yard.name,
                    style = Typography.titleMedium.copy(color = White)
                )
                Text(text = "Edit Data", style = Typography.labelSmall.copy(color = White))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = yard.description,
                style = Typography.bodyMedium.copy(color = White)
            )
            Spacer(modifier = Modifier.height(32.dp))
            DetailUserProduct(products, onClickAddProduct = { onClickAddProduct.invoke() })
        }
    }
}

@Composable()
fun DetailUserProduct(products: ArrayList<ProductModel>, onClickAddProduct: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                end = 16.dp
            ),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Product",
                style = Typography.titleMedium.copy(color = White)
            )
            Text(
                text = "Add Product",
                style = Typography.labelSmall.copy(color = White),
                modifier = Modifier.clickable {
                    onClickAddProduct.invoke()
                })
        }

        LazyColumn(
            modifier = Modifier.padding(bottom = 8.dp),
        ) {
            items(products.size) { item ->
                Box(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth()
                ) {
                    ProductItem(product = products[item], onClickItem = {})
                }
            }
        }
    }
}
