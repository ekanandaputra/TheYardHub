package com.ntech.theyardhub.feature.discussion

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.component.RoundedEditField
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Gray
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.model.DiscussionModel
import com.ntech.theyardhub.feature.chat.ChatMessageItem
import com.ntech.theyardhub.feature.chat.ChatViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionBottomSheet(
    onDismiss: () -> Unit,
    postId: String,
) {
    val coroutineScope = rememberCoroutineScope()
    val viewModel: DiscussionViewModel = get()
    val mContext = LocalContext.current
    val itemsList = remember { mutableStateListOf<DiscussionModel>() }
    val showDialog = remember { mutableStateOf(false) }
    val modalBottomSheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        viewModel.fetchDiscussions(postId = postId)
    }

    val contentInputState by viewModel.contentInputState
    val messageState = viewModel.messageLiveData.observeAsState().value
    val sendMessageState = viewModel.sendMessageLiveData.observeAsState().value

    showDialog.value = messageState is AppResponse.Loading
    showDialog.value = sendMessageState is AppResponse.Loading

    if (messageState is AppResponse.Success) {
        itemsList.addAll(messageState.data.filterNot { it in itemsList })
    }

    LaunchedEffect(sendMessageState) {
        if (sendMessageState is AppResponse.Success) {
            viewModel.fetchDiscussions(postId = postId)
        }
    }

    if (showDialog.value) LoadingDialog(setShowDialog = {})

    val sendIcon: Painter = painterResource(id = R.drawable.send)

    ModalBottomSheet(
        containerColor = White,
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(text = "Discussion", style = Typography.titleLarge.copy(color = Black))
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(itemsList.size) { item ->
                        DiscussionItem(
                            message = DiscussionModel(
                                sender = itemsList[item].sender,
                                content = itemsList[item].content,
                                dateTime = itemsList[item].dateTime,
                                isMyMessage = itemsList[item].isMyMessage,
                            )
                        )
                    }
                }
            }
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
                        value = contentInputState,
                        onValueChange = { value ->
                            viewModel.setContentInput(value)
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
                            viewModel.viewModelScope.launch {
                                viewModel.sendMessage(
                                    postId = postId,
                                    message = contentInputState.text
                                )
                            }
                            viewModel.setContentInput(TextFieldValue())
                        }
                )
            }
        }

    }
}
