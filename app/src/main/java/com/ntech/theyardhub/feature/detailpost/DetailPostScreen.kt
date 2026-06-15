package com.ntech.theyardhub.feature.detailpost

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.GeneralConfirmationBottomSheet
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.component.RoundedEditField
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.LoadImageWithGlide
import com.ntech.theyardhub.datalayer.model.DiscussionModel
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.feature.discussion.DiscussionItem
import com.ntech.theyardhub.feature.discussion.DiscussionViewModel
import com.ntech.theyardhub.feature.post.PostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPostScreen(navController: NavController, postId: String) {

    val coroutineScope = rememberCoroutineScope()
    val viewModel: DetailPostViewModel = get()
    val discussionViewModel: DiscussionViewModel = get()

    LaunchedEffect(Unit) {
        viewModel.fetchPost(postId)
        discussionViewModel.fetchDiscussions(postId)
    }

    val postState = viewModel.postLiveData.observeAsState().value
    val discussionsState = discussionViewModel.messageLiveData.observeAsState().value
    val sendMessageState = discussionViewModel.sendMessageLiveData.observeAsState().value
    val contentInputState by discussionViewModel.contentInputState
    val replyingTo by discussionViewModel.replyingToState

    val showDialog = remember { mutableStateOf(false) }
    val discussionsList = remember { mutableStateListOf<DiscussionModel>() }

    var post = PostModel("", "", "")
    
    when (postState) {
        is AppResponse.Loading -> {
            showDialog.value = true
        }
        is AppResponse.Success -> {
            showDialog.value = false
            post = postState.data
        }
        else -> {
            showDialog.value = false
        }
    }

    when (discussionsState) {
        is AppResponse.Success -> {
            discussionsList.clear()
            discussionsList.addAll(discussionsState.data)
        }
        else -> {}
    }

    LaunchedEffect(sendMessageState) {
        if (sendMessageState is AppResponse.Success) {
            discussionViewModel.fetchDiscussions(postId)
            discussionViewModel.setContentInput(TextFieldValue(""))
        }
    }

    if (showDialog.value) LoadingDialog(setShowDialog = {})

    Scaffold(
        containerColor = White,
        bottomBar = {
            if (!viewModel.getIsGuest()) {
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Column {
                            if (replyingTo != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(bluePrimary.copy(alpha = 0.05f))
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Membalas @${replyingTo?.sender}",
                                        style = Typography.labelSmall.copy(color = bluePrimary, fontWeight = FontWeight.Bold)
                                    )
                                    IconButton(
                                        onClick = { discussionViewModel.setReplyingTo(null) },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Cancel",
                                            tint = bluePrimary
                                        )
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .navigationBarsPadding()
                                    .imePadding(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    androidx.compose.material3.TextField(
                                        value = contentInputState,
                                        onValueChange = { discussionViewModel.setContentInput(it) },
                                        placeholder = { Text(if (replyingTo != null) "Tulis balasan..." else "Tambah komentar...") },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent,
                                            disabledContainerColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent
                                        ),
                                        textStyle = Typography.bodyMedium
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        if (contentInputState.text.isNotEmpty()) {
                                            coroutineScope.launch {
                                                discussionViewModel.sendMessage(postId, contentInputState.text)
                                                discussionViewModel.setReplyingTo(null)
                                            }
                                        }
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = bluePrimary,
                                        contentColor = White
                                    ),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Send",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                LoadImageWithGlide(
                    imageUrl = post.thumbnail,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                )
            }
            item {
                Column(Modifier.padding(24.dp)) {
                    Text(
                        text = post.title,
                        style = Typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold, color = Black)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = post.content,
                        style = Typography.bodyLarge.copy(lineHeight = 24.sp, color = Black.copy(alpha = 0.8f))
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Forum Diskusi",
                        style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = bluePrimary)
                    )
                }
            }

            if (discussionsList.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Belum ada komentar. Mulailah percakapan!",
                            style = Typography.bodyMedium.copy(color = Color.Gray),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                val parentComments = discussionsList.filter { it.parentCommentId == null }
                val replies = discussionsList.filter { it.parentCommentId != null }
                
                parentComments.forEach { parent ->
                    item {
                        DiscussionItem(
                            message = parent,
                            onReplyClicked = { discussionViewModel.setReplyingTo(it) }
                        )
                    }
                    val parentReplies = replies.filter { it.parentCommentId == parent.documentId }
                    items(parentReplies) { reply ->
                        DiscussionItem(
                            message = reply,
                            onReplyClicked = { discussionViewModel.setReplyingTo(it) }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

