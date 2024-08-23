package com.ntech.theyardhub.feature.detailpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.repository.PostRepository
import kotlinx.coroutines.launch

class DetailPostViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _postLiveData = MutableLiveData<AppResponse<PostModel>>()
    val postLiveData: LiveData<AppResponse<PostModel>> get() = _postLiveData

    suspend fun fetchPost(id: String) {
        viewModelScope.launch {
            _postLiveData.apply {
                postValue(AppResponse.Loading)
                val result = postRepository.getPost(id)
                postValue(result)
            }
        }
    }
}