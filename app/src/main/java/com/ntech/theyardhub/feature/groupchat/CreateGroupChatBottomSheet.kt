package com.ntech.theyardhub.feature.groupchat

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.component.RoundedEditField
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.utils.AppResponse
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupChatBottomSheet(
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val viewModel: GroupChatViewModel = get()
    val showDialog = remember { mutableStateOf(false) }
    val modalBottomSheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
    }

    val groupChatNameState by viewModel.groupChatNameState

    val createChatRoomState = viewModel.chatRoomLiveData.observeAsState().value

    showDialog.value = createChatRoomState is AppResponse.Loading

    if (createChatRoomState is AppResponse.Success) {
        onDismiss.invoke()
    }

    if (showDialog.value) LoadingDialog(setShowDialog = {})

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
            Text(text = "Create Group Chat", style = Typography.titleLarge.copy(color = Black))
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                RoundedEditField(
                    title = "Nama Group Chat",
                    value = groupChatNameState,
                    onValueChange = { value ->
                        viewModel.setGroupChatNameState(value)
                    },
                    hint = "Masukkan Nama Group"
                )
                Spacer(modifier = Modifier.height(24.dp))
                GeneralButton(
                    onButtonClicked = {
                        coroutineScope.launch {
                            viewModel.createChatRoom()
                        }
                    },
                    label = "Simpan",
                    buttonHeight = ButtonHeight.MEDIUM,
                    buttonType = ButtonType.PRIMARY,
                )
            }
        }

    }
}
