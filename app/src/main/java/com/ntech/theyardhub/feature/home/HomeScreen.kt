package com.ntech.theyardhub.feature.home

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Gray
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.utils.RoundedImageExample
import com.ntech.theyardhub.datalayer.model.LocationModel
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.YardModel

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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val image: Painter = painterResource(id = R.drawable.icon)
                        val sendIcon: Painter = painterResource(id = R.drawable.send)
                        Image(
                            painter = image,
                            contentDescription = "App Logo",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "YardHub",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(1f),
                            style = Typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                        )
                        Image(
                            painter = sendIcon,
                            contentDescription = "Send Icon",
                            modifier = Modifier.size(24.dp)
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
    val data: List<YardModel> = generateYardModels()

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Farms", style = Typography.titleMedium)
            Text(text = "See all", style = Typography.labelSmall)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(data.size) { index ->
                YardHomeItem(item = data[index], onClickItem = {})
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
fun YardHomeItem(item: YardModel, onClickItem: (YardModel) -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(White),
        modifier = Modifier
            .clickable { onClickItem.invoke(item) }
            .width(128.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = "https://images-squarespace--cdn-com.translate.goog/content/v1/552ed2d1e4b0745abca6723d/3e60e68a-5ee9-4f49-9261-e890a6673173/grape+3.jpg?format=2500w&_x_tr_sl=en&_x_tr_tl=id&_x_tr_hl=id&_x_tr_pto=tc",
                contentDescription = "Image from URL",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Color.Black),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = item.name, style = Typography.titleSmall.copy(
                        color = Black, fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.locationModel.city,
                    modifier = Modifier.padding(top = 5.dp),
                    style = Typography.bodyMedium
                )
            }
        }
    }
}

fun generateYardModels(): List<YardModel> {
    val yardModels = mutableListOf<YardModel>()

    yardModels.add(
        YardModel(
            "Kebun Anggur Makmur",
            "",
            "Description for Yard 1",
            LocationModel("Jakarta", -6.2088, 106.8456),
            "uuid-1"
        )
    )
    yardModels.add(
        YardModel(
            "Kebun Anggur Sejahtera",
            "",
            "Description for Yard 2",
            LocationModel("Bandung", -6.9175, 107.6191),
            "uuid-2"
        )
    )
    yardModels.add(
        YardModel(
            "Kebun Anggur A",
            "",
            "Description for Yard 3",
            LocationModel("Surabaya", -7.2504, 112.7688),
            "uuid-3"
        )
    )
    yardModels.add(
        YardModel(
            "Kebun Anggur B",
            "",
            "Description for Yard 4",
            LocationModel("Yogyakarta", -7.7956, 110.3695),
            "uuid-4"
        )
    )
    yardModels.add(
        YardModel(
            "Kebun Anggur D",
            "",
            "Description for Yard 5",
            LocationModel("Semarang", -6.9667, 110.4167),
            "uuid-5"
        )
    )
    return yardModels
}