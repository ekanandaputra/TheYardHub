package com.ntech.theyardhub.feature.login

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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()
    val viewModel: LoginViewModel = get()
    val mContext = LocalContext.current

    val usernameState by viewModel.usernameState
    val passwordState by viewModel.passwordState
    val isButtonNextEnable by derivedStateOf { viewModel.isButtonNextEnable }
    val loginState = viewModel.loginLiveData.observeAsState().value

    val showDialog = remember { mutableStateOf(false) }

    when (loginState) {
        is AppResponse.Loading -> {
            showDialog.value = true
        }

        is AppResponse.Empty -> {
            showDialog.value = false
            Toast.makeText(
                mContext,
                "Akun tidak terdaftar, Periksa kembali username dan password anda",
                Toast.LENGTH_SHORT
            ).show()
        }

        is AppResponse.Success -> {
            showDialog.value = false
            navController.navigate(HOME_SCREEN)
        }

        else -> {
            showDialog.value = false
        }
    }

    if (showDialog.value) LoadingDialog(setShowDialog = {})

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sign In") },
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
                text = "Welcome",
                style = Typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Login to your account",
            )
            Spacer(modifier = Modifier.height(32.dp))
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
                            viewModel.doLogin(
                                viewModel.usernameState.value.text,
                                viewModel.passwordState.value.text
                            )
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