package com.ntech.theyardhub.feature.detailyard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.datalayer.repository.YardRepository
import kotlinx.coroutines.launch

class DetailYardViewModel(
    private val yardRepository: YardRepository,
) : ViewModel() {

    private val _yardLiveData = MutableLiveData<AppResponse<YardModel>>()
    val yardLiveData: LiveData<AppResponse<YardModel>> get() = _yardLiveData

    suspend fun fetchYard(id: String) {
        viewModelScope.launch {
            _yardLiveData.apply {
                postValue(AppResponse.Loading)
                val result = yardRepository.getFarm(id)
                postValue(result)
            }
        }
    }
}