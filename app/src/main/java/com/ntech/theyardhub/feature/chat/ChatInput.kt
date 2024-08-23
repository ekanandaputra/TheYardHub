package com.ntech.theyardhub.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.RoundedEditField
import com.ntech.theyardhub.core.theme.Gray
import org.koin.androidx.compose.get

@Composable
fun ChatInput(onSendMessage: (String) -> Unit) {

    val coroutineScope = rememberCoroutineScope()
    val viewModel: ChatViewModel = get()
    val mContext = LocalContext.current

    val messageState by viewModel.messageState

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Gray)
    ) {
        RoundedEditField(
            title = "",
            value = messageState,
            onValueChange = { value ->
                viewModel.setMessage(value)
            },
            hint = "Masukkan Password",
            visualTransformation = PasswordVisualTransformation()
        )
        GeneralButton(
            onButtonClicked = {
//                coroutineScope.launch {
//                    viewModel.doLogin(
//                        viewModel.usernameState.value.text,
//                        viewModel.passwordState.value.text
//                    )
//                }
            },
            label = "Kirim",
            buttonType = ButtonType.PRIMARY,
            buttonHeight = ButtonHeight.MEDIUM,
            isEnabled = true,
        )
    }
}
