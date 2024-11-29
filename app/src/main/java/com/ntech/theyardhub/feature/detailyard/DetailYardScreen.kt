package com.ntech.theyardhub.feature.detailyard

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.RouteName.DETAIL_POST_SCREEN
import com.ntech.theyardhub.core.RouteName.DETAIL_PRODUCT_SCREEN
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.utils.LoadImageWithGlide
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.feature.product.ProductItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailYardScreen(navController: NavController) {

    val detailYard = YardModel(
        name = "Kebun Anggur Makmur",
        description = "Perkebunan anggur Makmur terletak di kawasan yang subur dan strategis, dengan pemandangan alam yang mempesona. Dikelola dengan penuh perhatian dan menggunakan metode pertanian yang ramah lingkungan, perkebunan ini menghasilkan anggur berkualitas tinggi yang terkenal akan rasa dan kesegarannya. Dengan berbagai varietas anggur yang ditanam, mulai dari anggur merah hingga anggur putih, setiap tanaman dirawat secara teliti agar dapat menghasilkan buah yang optimal."
    )
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
                            detailYard.name,
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
                .padding(
                    top = innerPadding.calculateTopPadding() + 8.dp,
                ),
        ) {
            LoadImageWithGlide(
                imageUrl = "https://images-squarespace--cdn-com.translate.goog/content/v1/552ed2d1e4b0745abca6723d/3e60e68a-5ee9-4f49-9261-e890a6673173/grape+3.jpg?format=2500w&_x_tr_sl=en&_x_tr_tl=id&_x_tr_hl=id&_x_tr_pto=tc",
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = detailYard.description,
                style = Typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(32.dp))
            DetailYardProduct(navController)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                GeneralButton(
                    onButtonClicked = { /*TODO*/ },
                    label = "Contact",
                    buttonType = ButtonType.PRIMARY,
                    buttonHeight = ButtonHeight.MEDIUM,
                )
            }
        }
    }
}

@Composable()
fun DetailYardProduct(navController: NavController) {
    val user = UserModel(
        id = "user-12345",
        name = "John Doe",
    )

    val products = listOf(
        ProductModel(
            uuid = "product-001",
            name = "Paket A",
            description = "Anggur merah segar yang dipanen langsung dari kebun.",
            price = 12000000,
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
        Text(
            text = "Product",
            style = Typography.titleLarge
        )
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
                    ProductItem(product = products[item], onClickItem = {
                        navController.navigate(
                            DETAIL_PRODUCT_SCREEN
                        )
                    })
                }
            }
        }
    }
}