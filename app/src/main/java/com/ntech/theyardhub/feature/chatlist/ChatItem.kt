package com.ntech.theyardhub.feature.chatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.utils.formatFirebaseTimestampToDate
import com.ntech.theyardhub.datalayer.model.ChatListModel
import com.ntech.theyardhub.datalayer.model.ChatMessageModel

@Composable
fun ChatItem(item: ChatListModel, onClickItem: (ChatListModel) -> Unit) {
    Column(
        Modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            .fillMaxWidth()
            .clickable {
                onClickItem.invoke(item)
            }
    ) {
        Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(bottom = 8.dp)) {
            Text(
                text = item.name,
                style = Typography.titleMedium.copy(
                    color = Black,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = formatFirebaseTimestampToDate(item.messageAt),
                modifier = Modifier.padding(top = 5.dp),
                style = Typography.bodySmall.copy(color = Color.Gray),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = item.message,
            modifier = Modifier.padding(top = 5.dp),
            style = Typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.Gray)
    )
}

@Preview(showBackground = true)
@Composable
fun ChatItemPreview() {
    ChatItem(
        item = ChatListModel(
            documentId = "",
            name = "Ekananda",
            message = "Selamat Pagi, "
        ),
        onClickItem = {}
    )
}