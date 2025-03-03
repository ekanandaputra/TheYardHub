package com.ntech.theyardhub.core.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.RouteName.LOGIN_SCREEN
import com.ntech.theyardhub.core.theme.Typography

@Composable
fun LoginAlert(
    onButtonClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                end = 16.dp,
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Silahkan Login Terlebih Dahulu",
            style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(24.dp))
        GeneralButton(
            onButtonClicked = { onButtonClicked.invoke() },
            label = "LOGIN",
            buttonType = ButtonType.PRIMARY,
            buttonHeight = ButtonHeight.LARGE,
        )
    }
}
