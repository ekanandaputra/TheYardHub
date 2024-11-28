package com.ntech.theyardhub.feature.chatlist

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.datalayer.model.ChatMessageModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavController) {

    val applicationContext = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val image: Painter = painterResource(id = R.drawable.icon)
                        Image(
                            painter = image,
                            contentDescription = "App Logo",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "Messages",
                            modifier = Modifier.padding(start = 16.dp),
                            style = Typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = White
                )
            )
        },
        containerColor = White,
    ) { innerPadding ->
        val itemList: List<ChatMessageModel> = generateFakeChatMessages()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                ),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    bottom = 80.dp, top = innerPadding.calculateTopPadding() + 24.dp,
                )
            ) {
                items(itemList.size) { item ->
                    ChatItem(
                        item = itemList[item],
                        onClickItem = {}
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

fun generateFakeChatMessages(): List<ChatMessageModel> {
    return listOf(
        ChatMessageModel(
            sender = "Farmer1",
            content = "Hello! How can I help you today?",
            dateTime = "2024-11-23T10:15:00",
            isMyMessage = false
        ),
        ChatMessageModel(
            sender = "Consumer1",
            content = "Hi! I want to know more about your organic products.",
            dateTime = "2024-11-23T10:16:00",
            isMyMessage = true
        ),
        ChatMessageModel(
            sender = "Farmer2",
            content = "Sure! We have fresh organic vegetables, fruits, and dairy products.",
            dateTime = "2024-11-23T10:17:00",
            isMyMessage = false
        ),
        ChatMessageModel(
            sender = "Consumer2",
            content = "Sounds great! Do you deliver to the city?",
            dateTime = "2024-11-23T10:18:00",
            isMyMessage = true
        ),
        ChatMessageModel(
            sender = "Farmer3",
            content = "Yes, we deliver to the city every weekend. Let me know what you'd like!",
            dateTime = "2024-11-23T10:19:00",
            isMyMessage = false
        )
    )
}