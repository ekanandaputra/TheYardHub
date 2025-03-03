package com.ntech.theyardhub.feature.detailgroupchat

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.feature.chat.ChatInput
import com.ntech.theyardhub.feature.chat.ChatMessageItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailGroupChatScreen(navController: NavController, roomId: String) {

    val coroutineScope = rememberCoroutineScope()
    val viewModel: DetailGroupChatViewModel = get()
    val mContext = LocalContext.current
    val itemsList = remember { mutableStateListOf<ChatMessageModel>() }
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchMessages(chatRoomId = roomId)
    }

    val messageState = viewModel.messageLiveData.observeAsState().value
    val sendMessageState = viewModel.sendMessageLiveData.observeAsState().value

    showDialog.value = messageState is AppResponse.Loading
    showDialog.value = sendMessageState is AppResponse.Loading

    if (messageState is AppResponse.Success) {
        itemsList.addAll(messageState.data.filterNot { it in itemsList })
    }

    if (sendMessageState is AppResponse.Success) {
        viewModel.viewModelScope.launch {
            viewModel.fetchMessages(chatRoomId = roomId)
        }
    }

    if (showDialog.value) LoadingDialog(setShowDialog = {})

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
                            roomId,
                            modifier = Modifier.padding(start = 8.dp),
                            style = Typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                )
            )
        },
        containerColor = White,
        bottomBar = {
            ChatInput { message ->
                viewModel.viewModelScope.launch {
                    viewModel.sendMessage(roomId, message)
                }
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

