package com.ntech.theyardhub.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.datalayer.repository.PostRepository
import com.ntech.theyardhub.datalayer.repository.YardRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val postRepository: PostRepository,
    private val yardRepository: YardRepository,
) : ViewModel() {

    private val _postLiveData = MutableLiveData<AppResponse<List<PostModel>>>()
    val postLiveData: LiveData<AppResponse<List<PostModel>>> get() = _postLiveData

    private val _yardLiveData = MutableLiveData<AppResponse<List<YardModel>>>()
    val yardLiveData: LiveData<AppResponse<List<YardModel>>> get() = _yardLiveData

    suspend fun fetchPosts() {
        viewModelScope.launch {
            _postLiveData.apply {
                postValue(AppResponse.Loading)
                val result = postRepository.getPosts()
                postValue(result)
            }
        }
    }

    suspend fun fetchYards() {
        viewModelScope.launch {
            _yardLiveData.apply {
                postValue(AppResponse.Loading)
                val result = yardRepository.getFarms()
                postValue(result)
            }
        }
    }
}