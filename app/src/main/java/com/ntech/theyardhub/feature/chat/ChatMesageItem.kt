package com.ntech.theyardhub.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Gray
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.theme.textGray
import com.ntech.theyardhub.datalayer.model.ChatMessageModel

@Composable
fun ChatMessageItem(message: ChatMessageModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalAlignment = if (message.isMyMessage) Alignment.End else Alignment.Start
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .background(color = if (message.isMyMessage) bluePrimary else Color.LightGray)
                    .fillMaxWidth(0.7f)
            ) {
                Text(
                    text = message.content,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    style = Typography.bodyMedium.copy(
                        color = if (message.isMyMessage) White else Black,
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = message.dateTime,
            style = Typography.bodySmall.copy(color = textGray)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun ChatMessageItemPreview() {
    ChatMessageItem(
        ChatMessageModel(
            "Ekananda",
            "Percobaan test message",
            dateTime = "",
            isMyMessage = true
        )
    )
}