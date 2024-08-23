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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import com.ntech.theyardhub.core.RouteName
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Gray
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.utils.LoadImageWithGlide
import com.ntech.theyardhub.core.utils.RoundedImageExample
import com.ntech.theyardhub.core.utils.toRupiahFormat
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.feature.bottomnavigation.BottomNavigationMenu

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailYardScreen(navController: NavController) {

    val usernameState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }

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
                            "Kebun Anggur Makmur",
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
            LoadImageWithGlide(
                imageUrl = "https://images-squarespace--cdn-com.translate.goog/content/v1/552ed2d1e4b0745abca6723d/3e60e68a-5ee9-4f49-9261-e890a6673173/grape+3.jpg?format=2500w&_x_tr_sl=en&_x_tr_tl=id&_x_tr_hl=id&_x_tr_pto=tc",
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Description()
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

@Composable
fun Description() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Description",
                style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Chevron Left",
                tint = bluePrimary,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Kebun Anggur Makmur adalah sebuah kebun anggur yang terletak di kawasan yang subur dan memiliki iklim yang ideal untuk pertumbuhan anggur berkualitas tinggi. Kebun ini dikenal dengan berbagai varietas anggur yang unggul, baik",
            style = Typography.bodySmall
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Location",
                style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Chevron Left",
                tint = bluePrimary,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.maps),
            contentDescription = "Rounded Image",
            contentScale = ContentScale.Crop // Crop the image if necessary
        )
    }
}

