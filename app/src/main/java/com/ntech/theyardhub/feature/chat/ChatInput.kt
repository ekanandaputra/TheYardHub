package com.ntech.theyardhub.feature.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ntech.theyardhub.R
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

    val sendIcon: Painter = painterResource(id = R.drawable.send)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            RoundedEditField(
                title = "",
                value = messageState,
                onValueChange = { value ->
                    viewModel.setMessage(value)
                },
                hint = "Enter Message"
            )
        }
        Image(
            painter = sendIcon,
            contentDescription = "Send Icon",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    onSendMessage.invoke(messageState.text)
                }
        )
    }
}
