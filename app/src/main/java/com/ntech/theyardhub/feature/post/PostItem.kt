package com.ntech.theyardhub.feature.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Gray
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.textGray
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.model.PostModel

@Composable
fun PostItem(post: PostModel, onClickItem: (PostModel) -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickItem.invoke(post) },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Column(
            Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(42.dp),
                    painter = painterResource(R.drawable.icon),
                    contentDescription = "avatar",
                    contentScale = ContentScale.FillBounds
                )
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(
                        text = post.title,
                        style = Typography.titleMedium.copy(
                            color = Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = post.content,
                        modifier = Modifier.padding(top = 5.dp),
                        style = Typography.bodyMedium
                    )

                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PostItemPreview() {
    PostItem(
        post = PostModel(
            thumbnail = "Ekananda",
            title = "Percobaan test message",
            content = "TES",
        ),
        onClickItem = {}
    )
}