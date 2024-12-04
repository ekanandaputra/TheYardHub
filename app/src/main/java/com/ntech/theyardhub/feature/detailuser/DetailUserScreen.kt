package com.ntech.theyardhub.feature.detailuser

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.RouteName
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Gray
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.theme.textGray
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.LoadImageWithGlide
import com.ntech.theyardhub.core.utils.RoundedImageExample
import com.ntech.theyardhub.core.utils.toRupiahFormat
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.feature.bottomnavigation.BottomNavigationMenu
import com.ntech.theyardhub.feature.detailpost.DetailPostViewModel
import com.ntech.theyardhub.feature.detailyard.DetailYardProduct
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
    }

    val userState = viewModel.userLiveData.observeAsState().value

    val showDialog = remember { mutableStateOf(false) }

    var data = UserModel()

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
                DetailProfile(data.yard)
            }

        }
    }
}

@Composable
fun DetailProfile(yard: YardModel) {
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
            DetailUserProduct()
        }
    }
}

@Composable()
fun DetailUserProduct() {
    val user = UserModel(
        id = "user-12345",
        name = "John Doe",
    )

    val products = listOf(
        ProductModel(
            uuid = "product-001",
            name = "Anggur Merah Segar",
            description = "Anggur merah segar yang dipanen langsung dari kebun.",
            price = 8000,
            user = user,
            userReference = user.id
        ),
        ProductModel(
            uuid = "product-002",
            name = "Anggur Putih Premium",
            description = "Anggur putih premium dengan kualitas terbaik.",
            price = 10000,
            user = user,
            userReference = user.id
        ),
        ProductModel(
            uuid = "product-003",
            name = "Anggur Kering untuk Roti",
            description = "Anggur kering kualitas tinggi untuk bahan roti dan kue.",
            price = 12000,
            user = user,
            userReference = user.id
        ),
        ProductModel(
            uuid = "product-004",
            name = "Anggur Organik",
            description = "Anggur yang ditanam tanpa penggunaan pestisida, cocok untuk konsumsi sehat.",
            price = 9500,
            user = user,
            userReference = user.id
        ),
        ProductModel(
            uuid = "product-005",
            name = "Anggur Beku untuk Smoothie",
            description = "Anggur beku yang siap digunakan dalam smoothie atau jus.",
            price = 11000,
            user = user,
            userReference = user.id
        ),
        ProductModel(
            uuid = "product-006",
            name = "Kerjasama Pengelolaan Kebun Anggur",
            description = "Program kerjasama untuk pengelolaan kebun anggur dengan pembagian hasil yang adil.",
            price = 0, // Kerjasama, bukan produk jual-beli
            user = user,
            userReference = user.id
        ),
        ProductModel(
            uuid = "product-007",
            name = "Anggur Kering untuk Camilan",
            description = "Anggur kering yang siap dikonsumsi sebagai camilan sehat.",
            price = 8500,
            user = user,
            userReference = user.id
        ),
        ProductModel(
            uuid = "product-008",
            name = "Pestisida Organik untuk Kebun Anggur",
            description = "Pestisida organik yang aman digunakan untuk merawat kebun anggur.",
            price = 15000,
            user = user,
            userReference = user.id
        ),
        ProductModel(
            uuid = "product-009",
            name = "Anggur dengan Teknik Hidroponik",
            description = "Anggur yang ditanam dengan metode hidroponik untuk hasil yang lebih efisien dan berkualitas.",
            price = 13000,
            user = user,
            userReference = user.id
        ),
        ProductModel(
            uuid = "product-010",
            name = "Paket Perawatan Kebun Anggur",
            description = "Paket lengkap untuk perawatan kebun anggur, termasuk pupuk dan peralatan.",
            price = 20000,
            user = user,
            userReference = user.id
        )
    )

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
            Text(text = "Add Product", style = Typography.labelSmall.copy(color = White))
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(bottom = 8.dp),
            userScrollEnabled = false,
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
