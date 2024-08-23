package com.ntech.theyardhub.feature.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.repository.PostRepository
import kotlinx.coroutines.launch

class PostViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _postLiveData = MutableLiveData<AppResponse<List<PostModel>>>()
    val postLiveData: LiveData<AppResponse<List<PostModel>>> get() = _postLiveData

    suspend fun fetchPosts() {
        viewModelScope.launch {
            _postLiveData.apply {
                postValue(AppResponse.Loading)
                val result = postRepository.getPosts()
                postValue(result)
            }
        }
    }
}