package com.ntech.theyardhub.feature.yards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.datalayer.repository.PostRepository
import com.ntech.theyardhub.datalayer.repository.YardRepository
import kotlinx.coroutines.launch

class YardViewModel(
    private val yardRepository: YardRepository,
    private val dataStorage: DataStorage,
) : ViewModel() {

    private val _yardLiveData = MutableLiveData<AppResponse<List<YardModel>>>()
    val yardLiveData: LiveData<AppResponse<List<YardModel>>> get() = _yardLiveData

    private var allYards: List<YardModel> = emptyList()

    fun fetchYards() {
        viewModelScope.launch {
            _yardLiveData.postValue(AppResponse.Loading)
            val result = yardRepository.getFarms()
            if (result is AppResponse.Success) {
                allYards = result.data.filter { it.userDocumentId != dataStorage.userDocumentId }
                _yardLiveData.postValue(AppResponse.Success(allYards))
            } else {
                _yardLiveData.postValue(result)
            }
        }
    }

    fun filterYards(city: String) {
        if (city == "Semua") {
            _yardLiveData.postValue(AppResponse.Success(allYards))
        } else {
            val filtered = allYards.filter { it.locationModel.city == city }
            _yardLiveData.postValue(AppResponse.Success(filtered))
        }
    }

    fun searchYards(query: String) {
        if (query.isEmpty()) {
            _yardLiveData.postValue(AppResponse.Success(allYards))
        } else {
            val filtered = allYards.filter { 
                it.name.contains(query, ignoreCase = true) || 
                it.description.contains(query, ignoreCase = true) ||
                it.locationModel.city.contains(query, ignoreCase = true)
            }
            _yardLiveData.postValue(AppResponse.Success(filtered))
        }
    }
}