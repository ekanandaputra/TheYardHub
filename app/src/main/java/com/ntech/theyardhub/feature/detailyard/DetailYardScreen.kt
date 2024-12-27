package com.ntech.theyardhub.feature.detailyard

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.RouteName.CHAT_SCREEN
import com.ntech.theyardhub.core.RouteName.DETAIL_POST_SCREEN
import com.ntech.theyardhub.core.RouteName.DETAIL_PRODUCT_SCREEN
import com.ntech.theyardhub.core.RouteName.DETAIL_YARD_SCREEN
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.LoadImageWithGlide
import com.ntech.theyardhub.datalayer.model.DiscussionModel
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.feature.detailpost.DetailPostViewModel
import com.ntech.theyardhub.feature.product.ProductItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailYardScreen(navController: NavController, yardId: String) {

    val coroutineScope = rememberCoroutineScope()
    val viewModel: DetailYardViewModel = get()
    val mContext = LocalContext.current
    val products = remember { mutableStateListOf<ProductModel>() }

    LaunchedEffect(Unit) {
        viewModel.fetchYard(yardId)
        viewModel.fetchProducts()
    }

    val yardState = viewModel.yardLiveData.observeAsState().value
    val chatRoomId = viewModel.chatRoomId.observeAsState().value
    val productState = viewModel.productsLiveData.observeAsState().value

    val showDialog = remember { mutableStateOf(false) }

    var data = YardModel()

    showDialog.value = yardState is AppResponse.Loading

    if (yardState is AppResponse.Success) {
        data = yardState.data
    }

    if (productState is AppResponse.Success) {
        products.addAll(productState.data.filterNot { it in products })
    }

    if (chatRoomId != null) {
        Log.d("TAG", "DetailYardScreen: " + chatRoomId.toString())
        navController.navigate("$CHAT_SCREEN/${chatRoomId.toString()}")
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
                            data.name,
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
        containerColor = White,
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(64.dp) // Adjust height as needed
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    GeneralButton(
                        onButtonClicked = {
                            CoroutineScope(Dispatchers.Main).launch {
                                viewModel.getOrCreateChatRoom(data.userDocumentId, data.ownerName)
                            }
                        },
                        label = "Chat with Owner",
                        buttonType = ButtonType.PRIMARY,
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
                .verticalScroll(scrollState)
                .padding(
                    top = innerPadding.calculateTopPadding() + 8.dp,
                ),
        ) {
            LoadImageWithGlide(
                imageUrl = data.thumbnail,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = data.description,
                style = Typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(32.dp))
            ProductsSection(products, navController)
        }
    }
}

@Composable()
fun ProductsSection(products: List<ProductModel>, navController: NavController) {
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
                style = Typography.titleMedium
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
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
