package com.ntech.theyardhub.feature.discussion

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.model.DiscussionModel
import com.ntech.theyardhub.datalayer.repository.ChatRepository
import com.ntech.theyardhub.datalayer.repository.PostRepository
import com.ntech.theyardhub.datalayer.repository.YardRepository
import kotlinx.coroutines.launch

class DiscussionViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {

    val contentInputState = mutableStateOf(TextFieldValue(""))

    fun setContentInput(newValue: TextFieldValue) {
        contentInputState.value = newValue
    }

    private val _messageLiveData = MutableLiveData<AppResponse<List<DiscussionModel>>>()
    val messageLiveData: LiveData<AppResponse<List<DiscussionModel>>> get() = _messageLiveData

    private val _sendMessageLiveData = MutableLiveData<AppResponse<DiscussionModel>>()
    val sendMessageLiveData: LiveData<AppResponse<DiscussionModel>> get() = _sendMessageLiveData

    suspend fun fetchDiscussions(postId: String) {
        viewModelScope.launch {
            _messageLiveData.apply {
                postValue(AppResponse.Loading)
                val result = postRepository.getDiscussion(postId)
                postValue(result)
            }
        }
    }

    suspend fun sendMessage(postId: String, message: String) {
        viewModelScope.launch {
            _sendMessageLiveData.apply {
                postValue(AppResponse.Loading)
                val result = postRepository.sendDiscussion(postDocumentId = postId, message = message)
                postValue(result)
            }
        }
    }
}