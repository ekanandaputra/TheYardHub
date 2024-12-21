package com.ntech.theyardhub.feature.chatlist

import android.provider.Telephony.Mms.Part
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.ChatListModel
import com.ntech.theyardhub.datalayer.model.ChatRoomModel
import com.ntech.theyardhub.datalayer.model.ParticipantDetailModel
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.datalayer.repository.ChatRepository
import com.ntech.theyardhub.datalayer.repository.ProductRepository
import com.ntech.theyardhub.datalayer.repository.YardRepository
import kotlinx.coroutines.launch
import kotlin.math.log

class ChatListviewModel(
    private val chatRepository: ChatRepository,
    private val dataStorage: DataStorage,
) : ViewModel() {

    private val _getChatRoomLiveData = MutableLiveData<AppResponse<List<ChatListModel>>>()
    val getChatRoomLiveData: LiveData<AppResponse<List<ChatListModel>>> get() = _getChatRoomLiveData

    suspend fun fetchChatRooms() {
        viewModelScope.launch {
            _getChatRoomLiveData.apply {
                postValue(AppResponse.Loading)
                val result = chatRepository.getChatRooms()
                postValue(result)
            }
        }
    }

}