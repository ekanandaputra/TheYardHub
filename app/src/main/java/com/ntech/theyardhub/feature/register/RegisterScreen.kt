package com.ntech.theyardhub.feature.register

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.RouteName.LOGIN_SCREEN
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.component.ModernTextField
import com.ntech.theyardhub.core.theme.AthensGray
import com.ntech.theyardhub.core.theme.Gray
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.utils.AppResponse
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@SuppressLint("UnrememberedMutableState")
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
            Toast.makeText(
                mContext,
                "Sucessfully Create Account",
                Toast.LENGTH_SHORT
            ).show()
            navController.navigate(LOGIN_SCREEN)
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
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                )
            )
        },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Header Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Create Account",
                    style = Typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = bluePrimary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Join us and start managing your yard today!",
                    style = Typography.bodyLarge.copy(color = Gray)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Registration Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ModernTextField(
                        value = nameState,
                        onValueChange = { viewModel.setName(it) },
                        label = "Full Name",
                        hint = "Enter your full name",
                        leadingIcon = Icons.Default.Person
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ModernTextField(
                        value = usernameState,
                        onValueChange = { viewModel.setUsername(it) },
                        label = "Username",
                        hint = "Enter your username",
                        leadingIcon = Icons.Default.Email
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ModernTextField(
                        value = passwordState,
                        onValueChange = { viewModel.setPassword(it) },
                        label = "Password",
                        hint = "Create a password",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    GeneralButton(
                        onButtonClicked = {
                            coroutineScope.launch {
                                viewModel.doRegister()
                            }
                        },
                        label = "Sign Up",
                        buttonType = ButtonType.PRIMARY,
                        buttonHeight = ButtonHeight.MEDIUM,
                        isEnabled = isButtonNextEnable,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "By signing up, you agree to our Terms and Conditions",
                        style = Typography.labelSmall.copy(color = Gray, textAlign = TextAlign.Center),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Already have an account? ",
                    style = Typography.bodyMedium
                )
                Text(
                    text = "Sign In",
                    style = Typography.bodyMedium.copy(
                        color = bluePrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable {
                        navController.navigate(LOGIN_SCREEN)
                    }
                )
            }
        }
    }
}
