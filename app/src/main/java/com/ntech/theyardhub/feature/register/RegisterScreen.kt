package com.ntech.theyardhub.feature.register

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.RouteName.HOME_SCREEN
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.component.RoundedEditField
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.feature.login.LoginViewModel
import com.ntech.theyardhub.feature.login.RegisterViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()
    val viewModel: RegisterViewModel = get()
    val mContext = LocalContext.current

    val usernameState by viewModel.usernameState
    val passwordState by viewModel.passwordState
    val nameState by viewModel.nameState

    val isButtonNextEnable by derivedStateOf { viewModel.isButtonNextEnable }
    val registerState = viewModel.registerLiveData.observeAsState().value

    val showDialog = remember { mutableStateOf(false) }

    when (registerState) {
        is AppResponse.Loading -> {
            showDialog.value = true
        }

        is AppResponse.Empty -> {
            showDialog.value = false
        }

        is AppResponse.Success -> {
            showDialog.value = false
        }

        is AppResponse.Error -> {
            showDialog.value = false
            Toast.makeText(
                mContext,
                registerState.message,
                Toast.LENGTH_SHORT
            ).show()
        }

        else -> {
            showDialog.value = false
        }
    }

    if (showDialog.value) LoadingDialog(setShowDialog = {})

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = White
                )
            )
        },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding() + 24.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
        ) {
            Text(
                text = "Register",
                style = Typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Isi semua form yang diperlukan",
            )
            Spacer(modifier = Modifier.height(32.dp))
            RoundedEditField(
                title = "Nama",
                value = nameState,
                onValueChange = { value ->
                    viewModel.setName(value)
                },
                hint = "Masukkan Nama"
            )
            Spacer(modifier = Modifier.height(16.dp))
            RoundedEditField(
                title = "Username",
                value = usernameState,
                onValueChange = { value ->
                    viewModel.setUsername(value)
                },
                hint = "Masukkan Username"
            )
            Spacer(modifier = Modifier.height(16.dp))
            RoundedEditField(
                title = "Password",
                value = passwordState,
                onValueChange = { value ->
                    viewModel.setPassword(value)
                },
                hint = "Masukkan Password",
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.40F)
                    .align(Alignment.End)
            ) {
                GeneralButton(
                    onButtonClicked = {
                        coroutineScope.launch {
                            viewModel.doRegister()
                        }
                    },
                    label = "Login",
                    buttonType = ButtonType.PRIMARY,
                    buttonHeight = ButtonHeight.MEDIUM,
                    isEnabled = isButtonNextEnable,
                )
            }
        }
    }
}