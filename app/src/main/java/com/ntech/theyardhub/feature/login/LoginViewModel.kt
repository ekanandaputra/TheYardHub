package com.ntech.theyardhub.feature.login

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.repository.AuthenticationRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository,
) : ViewModel() {

    val usernameState = mutableStateOf(TextFieldValue(""))
    val passwordState = mutableStateOf(TextFieldValue(""))

    private val _loginLiveData = MutableLiveData<AppResponse<UserModel>>()
    val loginLiveData: LiveData<AppResponse<UserModel>> get() = _loginLiveData

    suspend fun doLogin(username: String, password: String) {
        viewModelScope.launch {
            _loginLiveData.apply {
                postValue(AppResponse.Loading)
                val result = authenticationRepository.postLogin(username, password = password)
                postValue(result)
            }
        }
    }

    val isButtonNextEnable: Boolean
        get() = validateForm()

    fun setUsername(newValue: TextFieldValue) {
        usernameState.value = newValue
    }

    fun setPassword(newValue: TextFieldValue) {
        passwordState.value = newValue
    }

    private fun validateForm(): Boolean {
        return usernameState.value.text.isNotEmpty() && passwordState.value.text.isNotEmpty()
    }
}