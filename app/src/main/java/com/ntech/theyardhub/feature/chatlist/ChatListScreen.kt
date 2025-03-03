package com.ntech.theyardhub.feature.chatlist

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.RouteName
import com.ntech.theyardhub.core.RouteName.CHAT_SCREEN
import com.ntech.theyardhub.core.RouteName.LOGIN_SCREEN
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.LoginAlert
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ChatListModel
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.feature.yards.YardViewModel
import org.koin.androidx.compose.get

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavController) {

    val applicationContext = LocalContext.current
    val viewModel: ChatListviewModel = get()

    LaunchedEffect(Unit) {
        viewModel.fetchChatRooms()
    }

    val chatRoomStates = viewModel.getChatRoomLiveData.observeAsState().value

    var chatRooms = listOf<ChatListModel>()

    when (chatRoomStates) {
        is AppResponse.Loading -> {
        }

        is AppResponse.Empty -> {
        }

        is AppResponse.Success -> {
            chatRooms = chatRoomStates.data
        }

        else -> {
        }
    }

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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                )
            )
        },
        containerColor = White,
    ) { innerPadding ->
        if (viewModel.getIsGuest()) {
            LoginAlert(onButtonClicked = { navController.navigate(LOGIN_SCREEN) })
        } else {
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
                    items(chatRooms.size) { item ->
                        ChatItem(
                            item = chatRooms[item],
                            onClickItem = {
                                navController.navigate("$CHAT_SCREEN/${it.documentId}")
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

    }
}
