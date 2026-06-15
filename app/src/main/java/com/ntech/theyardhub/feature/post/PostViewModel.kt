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

    private var allPosts: List<PostModel> = emptyList()

    suspend fun fetchPosts() {
        viewModelScope.launch {
            _postLiveData.postValue(AppResponse.Loading)
            val result = postRepository.getPosts()
            if (result is AppResponse.Success) {
                allPosts = result.data
                _postLiveData.postValue(AppResponse.Success(allPosts))
            } else {
                _postLiveData.postValue(result)
            }
        }
    }

    fun filterPosts(category: String) {
        if (category == "Semua") {
            _postLiveData.postValue(AppResponse.Success(allPosts))
        } else {
            val filtered = allPosts.filter { it.category == category }
            _postLiveData.postValue(AppResponse.Success(filtered))
        }
    }

    fun searchPosts(query: String) {
        if (query.isEmpty()) {
            _postLiveData.postValue(AppResponse.Success(allPosts))
        } else {
            val filtered = allPosts.filter { 
                it.title.contains(query, ignoreCase = true) || 
                it.content.contains(query, ignoreCase = true) 
            }
            _postLiveData.postValue(AppResponse.Success(filtered))
        }
    }
}