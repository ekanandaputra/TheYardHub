package com.ntech.theyardhub.feature.chat

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository,
) : ViewModel() {

    val messageState = mutableStateOf(TextFieldValue(""))

    fun setMessage(newValue: TextFieldValue) {
        messageState.value = newValue
    }

    private val _messageLiveData = MutableLiveData<AppResponse<List<ChatMessageModel>>>()
    val messageLiveData: LiveData<AppResponse<List<ChatMessageModel>>> get() = _messageLiveData

    private val _sendMessageLiveData = MutableLiveData<AppResponse<ChatMessageModel>>()
    val sendMessageLiveData: LiveData<AppResponse<ChatMessageModel>> get() = _sendMessageLiveData

    suspend fun fetchHistory(chatRoomId: String) {
        viewModelScope.launch {
            _messageLiveData.apply {
                postValue(AppResponse.Loading)
                val result = chatRepository.getChatMessage(chatRoomId)
                postValue(result)
            }
        }
    }

    suspend fun sendMessage(chatRoomId: String, message: String) {
        viewModelScope.launch {
            _sendMessageLiveData.apply {
                postValue(AppResponse.Loading)
                val result = chatRepository.sendMessage(message, chatRoomId)
                postValue(result)
            }
        }
    }
}