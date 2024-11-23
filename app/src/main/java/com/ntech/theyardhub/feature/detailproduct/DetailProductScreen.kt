package com.ntech.theyardhub.feature.detailproduct

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.utils.LoadImageWithGlide
import com.ntech.theyardhub.core.utils.toRupiahFormat
import com.ntech.theyardhub.datalayer.model.ProductModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProductScreen(navController: NavController) {

    var product = ProductModel("", "Paket A", "Nikmati keindahan alam dan manisnya hasil panen dari Kebun Anggur Kami! Kami menyediakan berbagai jenis anggur berkualitas tinggi yang tumbuh dengan cinta dan perhatian. Anggur kami ditanam secara organik di tanah subur, dengan metode ramah lingkungan untuk memastikan rasa yang terbaik dan kemurnian alami.", 12000000)

    Scaffold(
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
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
        ) {
            LoadImageWithGlide(
                imageUrl = "https://images-squarespace--cdn-com.translate.goog/content/v1/552ed2d1e4b0745abca6723d/3e60e68a-5ee9-4f49-9261-e890a6673173/grape+3.jpg?format=2500w&_x_tr_sl=en&_x_tr_tl=id&_x_tr_hl=id&_x_tr_pto=tc",
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = product.name,
                    style = Typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = product.price.toRupiahFormat(),
                    style = Typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = product.description,
                    style = Typography.bodyMedium
                )
            }
        }
    }
}

