package com.ntech.theyardhub.feature.detailgroupchat

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.repository.ChatRepository
import com.ntech.theyardhub.datalayer.repository.GroupChatRepository
import kotlinx.coroutines.launch

class DetailGroupChatViewModel(
    private val groupChatRepository: GroupChatRepository,
) : ViewModel() {

    val messageState = mutableStateOf(TextFieldValue(""))

    fun setMessage(newValue: TextFieldValue) {
        messageState.value = newValue
    }

    private val _messageLiveData = MutableLiveData<AppResponse<List<ChatMessageModel>>>()
    val messageLiveData: LiveData<AppResponse<List<ChatMessageModel>>> get() = _messageLiveData

    private val _sendMessageLiveData = MutableLiveData<AppResponse<ChatMessageModel>>()
    val sendMessageLiveData: LiveData<AppResponse<ChatMessageModel>> get() = _sendMessageLiveData

    suspend fun fetchMessages(chatRoomId: String) {
        viewModelScope.launch {
            _messageLiveData.apply {
                postValue(AppResponse.Loading)
                val result = groupChatRepository.getMessages(chatRoomId)
                postValue(result)
            }
        }
    }

    suspend fun sendMessage(chatRoomId: String, message: String) {
        viewModelScope.launch {
            _sendMessageLiveData.apply {
                postValue(AppResponse.Loading)
                val result =
                    groupChatRepository.sendMessage(groupChatRoomId = chatRoomId, message = message)
                postValue(result)
            }
        }
    }
}