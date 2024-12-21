package com.ntech.theyardhub.feature.detailyard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.ChatRoomModel
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.datalayer.repository.ChatRepository
import com.ntech.theyardhub.datalayer.repository.ProductRepository
import com.ntech.theyardhub.datalayer.repository.YardRepository
import kotlinx.coroutines.launch
import kotlin.math.log

class DetailYardViewModel(
    private val yardRepository: YardRepository,
    private val productRepository: ProductRepository,
    private val chatRepository: ChatRepository,
    private val dataStorage: DataStorage,
) : ViewModel() {

    private val _yardLiveData = MutableLiveData<AppResponse<YardModel>>()
    val yardLiveData: LiveData<AppResponse<YardModel>> get() = _yardLiveData

    private val _productsLiveData = MutableLiveData<AppResponse<List<ProductModel>>>()
    val productsLiveData: LiveData<AppResponse<List<ProductModel>>> get() = _productsLiveData

    private val _createChatRoomViewModel = MutableLiveData<AppResponse<ChatRoomModel>>()
    val createChatRoomViewModel: LiveData<AppResponse<ChatRoomModel>> get() = _createChatRoomViewModel

    private val _getChatRoomLiveData = MutableLiveData<AppResponse<ChatRoomModel>>()
    val getChatRoomLiveData: LiveData<AppResponse<ChatRoomModel>> get() = _getChatRoomLiveData

    private val _chatRoomId = MutableLiveData<String>()
    val chatRoomId: LiveData<String> get() = _chatRoomId

    suspend fun fetchYard(id: String) {
        viewModelScope.launch {
            _yardLiveData.apply {
                postValue(AppResponse.Loading)
                val result = yardRepository.getFarm(id)
                postValue(result)
            }
        }
    }

    suspend fun fetchProducts(userDocumentId: String) {
        viewModelScope.launch {
            _productsLiveData.apply {
                postValue(AppResponse.Loading)
                val result = productRepository.getProductsByUserId(userDocumentId)
                postValue(result)
            }
        }
    }

    suspend fun createChatRoom(destinationUserDocumentId: String) {
        val request: ChatRoomModel = ChatRoomModel(
            participants = listOf(destinationUserDocumentId, dataStorage.userDocumentId).sorted(),
            createdAt = Timestamp.now()
        )
        viewModelScope.launch {
            _createChatRoomViewModel.apply {
                postValue(AppResponse.Loading)
                val result = chatRepository.createChatRoom(request)
                postValue(result)
            }
        }
    }

    suspend fun getChatRoomByParticipants(destinationUserDocumentId: String) {
        Log.d("TAG", "getChatRoomByParticipants: " + destinationUserDocumentId)
        val participants = listOf(destinationUserDocumentId, dataStorage.userDocumentId).sorted()
        viewModelScope.launch {
            _getChatRoomLiveData.apply {
                postValue(AppResponse.Loading)
                val result = chatRepository.getChatRoomByParticipantId(participants)
                Log.d("TAG", "getChatRoomByParticipants: " + result)
                postValue(result)
            }
        }
    }

    suspend fun getOrCreateChatRoom(destinationUserDocumentId: String) {
        val participants = listOf(destinationUserDocumentId, dataStorage.userDocumentId).sorted()

        viewModelScope.launch {
            _getChatRoomLiveData.postValue(AppResponse.Loading)

            // Step 1: Check if the chat room exists
            val existingRoom = chatRepository.getChatRoomByParticipantId(participants)

            if (existingRoom is AppResponse.Success && existingRoom.data != null) {
                // Chat room exists
                Log.d("TAG", "Chat room found: ${existingRoom.data}")
                _getChatRoomLiveData.postValue(existingRoom)
                _chatRoomId.postValue(existingRoom.data.documentId)
            } else {
                // Chat room does not exist, create a new one
                Log.d("TAG", "No chat room found. Creating a new one.")

                val newChatRoom = ChatRoomModel(
                    participants = participants,
                    createdAt = Timestamp.now()
                )

                _createChatRoomViewModel.postValue(AppResponse.Loading)

                val creationResult = chatRepository.createChatRoom(newChatRoom)

                if (creationResult is AppResponse.Success) {
                    Log.d("TAG", "Chat room created: ${creationResult.data}")
                    _getChatRoomLiveData.postValue(creationResult)
                    _chatRoomId.postValue(creationResult.data.documentId)
                } else {
                    // Handle creation error
                    Log.d("TAG", "Error creating chat room: ${creationResult}")
                    _getChatRoomLiveData.postValue(creationResult)
                }
            }
        }
    }
}