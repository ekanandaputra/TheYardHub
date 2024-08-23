package com.ntech.theyardhub.feature.login

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.RegisterRequest
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.repository.AuthenticationRepository
import kotlinx.coroutines.launch
import java.util.UUID

class RegisterViewModel(
    private val authenticationRepository: AuthenticationRepository,
) : ViewModel() {

    val usernameState = mutableStateOf(TextFieldValue(""))
    val passwordState = mutableStateOf(TextFieldValue(""))
    val nameState = mutableStateOf(TextFieldValue(""))

    private val _registerLiveData = MutableLiveData<AppResponse<UserModel>>()
    val registerLiveData: LiveData<AppResponse<UserModel>> get() = _registerLiveData

    suspend fun doRegister() {
        viewModelScope.launch {
            _registerLiveData.apply {
                postValue(AppResponse.Loading)
                val result = authenticationRepository.postRegister(
                    RegisterRequest(
                        uuid = UUID.randomUUID().toString(),
                        username = usernameState.value.text,
                        password = passwordState.value.text,
                        name = nameState.value.text,
                    )
                )
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

    fun setName(newValue: TextFieldValue) {
        nameState.value = newValue
    }

    private fun validateForm(): Boolean {
        return usernameState.value.text.isNotEmpty() &&
                passwordState.value.text.isNotEmpty() &&
                nameState.value.text.isNotEmpty()
    }
}