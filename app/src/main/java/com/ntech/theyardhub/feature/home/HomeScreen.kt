package com.ntech.theyardhub.feature.home

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Gray
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.utils.RoundedImageExample
import com.ntech.theyardhub.core.utils.toRupiahFormat
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.ProductModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    val usernameState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val image: Painter = painterResource(id = R.drawable.icon)
                        Image(
                            painter = image,
                            contentDescription = "App Logo",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "YardHub",
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
                    top = innerPadding.calculateTopPadding() + 24.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
        ) {
            HeaderHome()
            Spacer(modifier = Modifier.height(32.dp))
            ProductHome()
            Spacer(modifier = Modifier.height(32.dp))
            ArticleHome()
        }
    }
}

@Composable
fun HeaderHome() {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(text = "Hello Ekananda", style = Typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "We bring the best for you",
                style = Typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = bluePrimary
                )
            )
        }
        RoundedImageExample()
    }
}

@Composable
fun ArticleHome() {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Article for You", style = Typography.titleMedium)
            Text(text = "See all article", style = Typography.labelSmall)
        }
        Spacer(modifier = Modifier.height(8.dp))
        ArticleHomeItem(
            PostModel(
                title = "MANFAAT TRICHODERMA SP & CARA PEMBIAKKANNYA",
                content = "Pupuk Bioligis dan Biofungisida Trichoderma, sp Ketergantungan kita terhadap bahan-bahan kimia (pupuk kimia) apalagi bahan yang bersifat racun (insektisida, fungisida, bakterisida) harus segera kita tinggalkan. Kita harus menggali bahan-bahan disekitar kita yang bisa kita manfaatkan untuk mengga"
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        ArticleHomeItem(
            PostModel(
                title = "MANFAAT TRICHODERMA SP & CARA PEMBIAKKANNYA",
                content = "Pupuk Bioligis dan Biofungisida Trichoderma, sp Ketergantungan kita terhadap bahan-bahan kimia (pupuk kimia) apalagi bahan yang bersifat racun (insektisida, fungisida, bakterisida) harus segera kita tinggalkan. Kita harus menggali bahan-bahan disekitar kita yang bisa kita manfaatkan untuk mengga"
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        ArticleHomeItem(
            PostModel(
                title = "MANFAAT TRICHODERMA SP & CARA PEMBIAKKANNYA",
                content = "Pupuk Bioligis dan Biofungisida Trichoderma, sp Ketergantungan kita terhadap bahan-bahan kimia (pupuk kimia) apalagi bahan yang bersifat racun (insektisida, fungisida, bakterisida) harus segera kita tinggalkan. Kita harus menggali bahan-bahan disekitar kita yang bisa kita manfaatkan untuk mengga"
            )
        )
    }
}

@Composable
fun ProductHome() {
    val data: ArrayList<ProductModel> = arrayListOf(
        ProductModel(name = "Product 1", price = 100000),
        ProductModel(name = "Product 2", price = 250000),
        ProductModel(name = "Product 3", price = 1250000),
        ProductModel(name = "Product 4", price = 3000000),
        ProductModel(name = "Product 5", price = 500000)
    )

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Recommended Product", style = Typography.titleMedium)
            Text(text = "See all", style = Typography.labelSmall)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(data.size) { index ->
                ProductHomeItem(product = data[index], onClickItem = {})
            }
        }
    }
}

@Composable
fun ArticleHomeItem(post: PostModel = PostModel()) {
    Box(
        modifier = Modifier
            .border(
                BorderStroke(0.5.dp, Gray),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            val image: Painter = painterResource(id = R.drawable.icon)
            Image(
                painter = image,
                contentDescription = "App Logo",
                modifier = Modifier.size(72.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
            ) {
                Text(text = post.title, style = Typography.titleSmall)
                Text(
                    text = post.content,
                    style = Typography.labelSmall.copy(color = Gray),
                    modifier = Modifier.padding(top = 8.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ProductHomeItem(product: ProductModel, onClickItem: (ProductModel) -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(White),
        modifier = Modifier
            .clickable { onClickItem.invoke(product) }
            .width(128.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(R.drawable.icon),
                contentDescription = "avatar",
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = product.name, style = Typography.titleSmall.copy(
                    color = Black, fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.price.toRupiahFormat(),
                modifier = Modifier.padding(top = 5.dp),
                style = Typography.bodyMedium
            )
        }
    }
}
