package com.ntech.theyardhub.feature.discussion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.ntech.theyardhub.core.theme.*
import com.ntech.theyardhub.core.utils.formatFirebaseTimestampToDate
import com.ntech.theyardhub.datalayer.model.DiscussionModel

@Composable
fun DiscussionItem(
    message: DiscussionModel,
    onReplyClicked: (DiscussionModel) -> Unit = {}
) {
    val isReply = message.parentCommentId != null
    val isMine = message.isMyMessage == true
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 16.dp,
                start = if (isReply) 48.dp else 16.dp,
                end = 16.dp
            ),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        if (!isMine) {
            DiscussionAvatar(name = message.sender)
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column(
            modifier = Modifier.weight(1f, fill = false),
            horizontalAlignment = if (isMine) Alignment.End else Alignment.Start
        ) {
            if (!isMine && !isReply) {
                Text(
                    text = message.sender,
                    style = Typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Black.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
            }

            Card(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isMine) 16.dp else 4.dp,
                    bottomEnd = if (isMine) 4.dp else 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isMine) bluePrimary else Color(0xFFF2F2F2)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp)) {
                    if (isReply) {
                        Text(
                            text = "Replying to @${message.replyToName}",
                            style = Typography.labelSmall.copy(
                                color = if (isMine) White.copy(alpha = 0.8f) else bluePrimary,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    Text(
                        text = message.content,
                        style = Typography.bodyMedium.copy(
                            color = if (isMine) White else Black,
                            lineHeight = 20.sp
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(
                    text = formatFirebaseTimestampToDate(message.dateTime),
                    style = Typography.labelSmall.copy(color = textGray.copy(alpha = 0.6f))
                )
                if (!isMine) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Reply",
                        style = Typography.labelSmall.copy(
                            color = bluePrimary, 
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.clickable { onReplyClicked(message) }
                    )
                }
            }
        }

        if (isMine) {
            Spacer(modifier = Modifier.width(12.dp))
            DiscussionAvatar(name = message.sender)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatMessageItemPreview() {
    Column {
        DiscussionItem(
            DiscussionModel(
                sender = "Ekananda",
                content = "Ini adalah pesan percobaan untuk melihat tampilan baru yang lebih modern.",
                dateTime = Timestamp.now(),
                isMyMessage = false
            )
        )
        DiscussionItem(
            DiscussionModel(
                sender = "Me",
                content = "Sangat keren! Desainnya terasa jauh lebih segar.",
                dateTime = Timestamp.now(),
                isMyMessage = true,
                parentCommentId = "1",
                replyToName = "Ekananda"
            )
        )
    }
}
