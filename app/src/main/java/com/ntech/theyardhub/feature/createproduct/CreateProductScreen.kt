package com.ntech.theyardhub.feature.createproduct

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.RoundedEditField
import com.ntech.theyardhub.core.theme.White

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Product") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = White
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                GeneralButton(
                    onButtonClicked = {
                    },
                    label = "Create Product",
                    buttonType = ButtonType.PRIMARY,
                    buttonHeight = ButtonHeight.MEDIUM,
                    isEnabled = true,
                )
            }
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Tap to select an image", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            RoundedEditField(
                title = "Title",
                value = TextFieldValue(),
                onValueChange = { value ->
                },
                hint = "Enter Title"
            )
            Spacer(modifier = Modifier.height(16.dp))
            RoundedEditField(
                title = "Description",
                value = TextFieldValue(),
                onValueChange = { value ->
                },
                hint = "Enter Description",
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            RoundedEditField(
                title = "Price",
                value = TextFieldValue(),
                onValueChange = { value ->
                },
                hint = "Enter Price",
            )
        }
    }
}