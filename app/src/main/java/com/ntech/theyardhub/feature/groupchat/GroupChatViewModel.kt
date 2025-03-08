package com.ntech.theyardhub.feature.groupchat

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.GroupChatRoomModel
import com.ntech.theyardhub.datalayer.repository.GroupChatRepository
import kotlinx.coroutines.launch

class GroupChatViewModel(
    private val groupChatRepository: GroupChatRepository,
    private val dataStorage: DataStorage,
) : ViewModel() {

    val groupChatNameState = mutableStateOf(TextFieldValue(""))

    private val _getChatRoomLiveData = MutableLiveData<AppResponse<List<GroupChatRoomModel>>>()
    val getChatRoomLiveData: LiveData<AppResponse<List<GroupChatRoomModel>>> get() = _getChatRoomLiveData

    private val _chatRoomLiveData = MutableLiveData<AppResponse<GroupChatRoomModel>>()
    val chatRoomLiveData: LiveData<AppResponse<GroupChatRoomModel>> get() = _chatRoomLiveData

    suspend fun fetchChatRooms() {
        viewModelScope.launch {
            _getChatRoomLiveData.apply {
                postValue(AppResponse.Loading)
                val result = groupChatRepository.getGroupChatRooms()
                postValue(result)
            }
        }
    }

    suspend fun createChatRoom() {
        viewModelScope.launch {
            _chatRoomLiveData.apply {
                postValue(AppResponse.Loading)
                val result = groupChatRepository.createChatRoom(groupChatNameState.value.text)
                postValue(result)
            }
        }
    }

    fun getIsGuest(): Boolean {
        return dataStorage.isGuest
    }

    fun setGroupChatNameState(newValue: TextFieldValue) {
        groupChatNameState.value = newValue
    }
}