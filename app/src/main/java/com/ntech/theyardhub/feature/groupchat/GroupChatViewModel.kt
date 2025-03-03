package com.ntech.theyardhub.feature.groupchat

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

    private val _getChatRoomLiveData = MutableLiveData<AppResponse<List<GroupChatRoomModel>>>()
    val getChatRoomLiveData: LiveData<AppResponse<List<GroupChatRoomModel>>> get() = _getChatRoomLiveData

    suspend fun fetchChatRooms() {
        viewModelScope.launch {
            _getChatRoomLiveData.apply {
                postValue(AppResponse.Loading)
                val result = groupChatRepository.getGroupChatRooms()
                postValue(result)
            }
        }
    }

    fun getIsGuest(): Boolean {
        return dataStorage.isGuest
    }
}