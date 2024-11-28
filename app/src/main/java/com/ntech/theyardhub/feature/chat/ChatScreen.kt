package com.ntech.theyardhub.feature.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import org.koin.androidx.compose.get

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()
    val viewModel: ChatViewModel = get()
    val mContext = LocalContext.current
    val itemsList: List<ChatMessageModel> = populateData()

//    LaunchedEffect(Unit) {
//        viewModel.fetchHistory()
//    }
//
//    val messageState = viewModel.messageLiveData.observeAsState().value
//
//    val showDialog = remember { mutableStateOf(false) }
//
//    when (messageState) {
//        is AppResponse.Loading -> {
//            showDialog.value = true
//        }
//
//        is AppResponse.Empty -> {
//            showDialog.value = false
//        }
//
//        is AppResponse.Success -> {
//            showDialog.value = false
//            itemsList = messageState.data as ArrayList<ChatMessageModel>
//        }
//
//        else -> {
//            showDialog.value = false
//        }
//    }
//
//    if (showDialog.value) LoadingDialog(setShowDialog = {})

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = "Chevron Left"
                        )
                        Text(
                            "Farmer1",
                            modifier = Modifier.padding(start = 8.dp),
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
        bottomBar = {
            ChatInput {

            }
        }
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(itemsList.size) { item ->
                    ChatMessageItem(
                        message = ChatMessageModel(
                            sender = itemsList[item].sender,
                            content = itemsList[item].content,
                            dateTime = itemsList[item].dateTime,
                            isMyMessage = itemsList[item].isMyMessage,
                        )
                    )
                }
            }
        }
    }
}

fun populateData(): List<ChatMessageModel> {
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

