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

    suspend fun fetchHistory() {
        viewModelScope.launch {
            _messageLiveData.apply {
                postValue(AppResponse.Loading)
                val result = chatRepository.getChatMessage()
                postValue(result)
            }
        }
    }
}