package com.ntech.theyardhub.feature.yards

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

class YardViewModel(
    private val yardRepository: YardRepository,
) : ViewModel() {

    private val _yardLiveData = MutableLiveData<AppResponse<List<YardModel>>>()
    val yardLiveData: LiveData<AppResponse<List<YardModel>>> get() = _yardLiveData

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