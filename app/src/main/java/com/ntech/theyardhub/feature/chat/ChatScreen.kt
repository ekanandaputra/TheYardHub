package com.ntech.theyardhub.feature.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.component.RoundedEditField
import com.ntech.theyardhub.core.theme.Gray
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()
    val viewModel: ChatViewModel = get()
    val mContext = LocalContext.current
    var itemsList = arrayListOf<ChatMessageModel>()

    LaunchedEffect(Unit) {
        viewModel.fetchHistory()
    }

    val messageState = viewModel.messageLiveData.observeAsState().value

    val showDialog = remember { mutableStateOf(false) }

    when (messageState) {
        is AppResponse.Loading -> {
            showDialog.value = true
        }

        is AppResponse.Empty -> {
            showDialog.value = false
        }

        is AppResponse.Success -> {
            showDialog.value = false
            itemsList = messageState.data as ArrayList<ChatMessageModel>
        }

        else -> {
            showDialog.value = false
        }
    }

    if (showDialog.value) LoadingDialog(setShowDialog = {})


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
                            "YardHub",
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
        containerColor = White
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
//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                reverseLayout = true
//            ) {
//                items(itemsList.size) { item ->
//                    ChatMessageItem(
//                        message = ChatMessageModel(
//                            sender = itemsList[item].sender,
//                            content = itemsList[item].content,
//                            dateTime = itemsList[item].dateTime,
//                            isMyMessage = itemsList[item].isMyMessage,
//                        )
//                    )
//                }
//            }
            ChatInput(onSendMessage = {})
        }
    }
}

