package com.ntech.theyardhub.feature.detailpost

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.GeneralConfirmationBottomSheet
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.LoadImageWithGlide
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.feature.discussion.DiscussionBottomSheet
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
    val mContext = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchPost(postId)
    }

    val postState = viewModel.postLiveData.observeAsState().value

    val showDialog = remember { mutableStateOf(false) }

    var post = PostModel("", "", "")
    when (postState) {
        is AppResponse.Loading -> {
            showDialog.value = true
        }

        is AppResponse.Empty -> {
            showDialog.value = false
        }

        is AppResponse.Success -> {
            showDialog.value = false
            post = postState.data
        }

        else -> {
            showDialog.value = false
        }
    }

    if (showDialog.value) LoadingDialog(setShowDialog = {})

    val scrollState = rememberScrollState()

    val showSheetDiscussion = remember { mutableStateOf(false) }


    if (showSheetDiscussion.value) {
        DiscussionBottomSheet(onDismiss = { showSheetDiscussion.value = false }, postId = postId)
    }

    Scaffold(
        containerColor = White,
        bottomBar = {
            if (!viewModel.getIsGuest()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    GeneralButton(
                        onButtonClicked = {
                            showSheetDiscussion.value = true
                        },
                        label = "Discussion",
                        buttonType = ButtonType.PRIMARY,
                        buttonHeight = ButtonHeight.MEDIUM,
                        isEnabled = true,
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            LoadImageWithGlide(
                imageUrl = "https://images-squarespace--cdn-com.translate.goog/content/v1/552ed2d1e4b0745abca6723d/3e60e68a-5ee9-4f49-9261-e890a6673173/grape+3.jpg?format=2500w&_x_tr_sl=en&_x_tr_tl=id&_x_tr_hl=id&_x_tr_pto=tc",
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = post.title,
                    style = Typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = post.content,
                    style = Typography.bodyMedium
                )
            }
        }
    }
}

