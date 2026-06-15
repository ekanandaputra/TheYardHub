package com.ntech.theyardhub.feature.login

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.RouteName.HOME_SCREEN
import com.ntech.theyardhub.core.RouteName.REGISTER_SCREEN
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
            viewModel.setIsGuest(false)
            navController.navigate(HOME_SCREEN)
        }

        else -> {
            showDialog.value = false
        }
    }

    if (showDialog.value) LoadingDialog(setShowDialog = {})

    Scaffold(
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // Branding Section
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(bluePrimary, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "App Logo",
                    modifier = Modifier.size(64.dp),
                    tint = White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "The Yard Hub",
                style = Typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = bluePrimary
                )
            )
            Text(
                text = "Menghubungkan Lahan & Masyarakat",
                style = Typography.bodyMedium.copy(color = Gray)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Login Card
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
                    Text(
                        text = "Masuk",
                        style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Silakan masukkan akun Anda untuk masuk",
                        style = Typography.bodyMedium.copy(color = Gray),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    ModernTextField(
                        value = usernameState,
                        onValueChange = { viewModel.setUsername(it) },
                        label = "Username",
                        hint = "Masukkan username Anda",
                        leadingIcon = Icons.Default.Person
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ModernTextField(
                        value = passwordState,
                        onValueChange = { viewModel.setPassword(it) },
                        label = "Kata Sandi",
                        hint = "Masukkan kata sandi Anda",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "Lupa Kata Sandi?",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        style = Typography.labelMedium.copy(color = bluePrimary, fontWeight = FontWeight.SemiBold)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    GeneralButton(
                        onButtonClicked = {
                            coroutineScope.launch {
                                viewModel.doLogin(
                                    viewModel.usernameState.value.text,
                                    viewModel.passwordState.value.text
                                )
                            }
                        },
                        label = "Masuk",
                        buttonHeight = ButtonHeight.MEDIUM,
                        isEnabled = isButtonNextEnable,
                        buttonType = ButtonType.PRIMARY,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Gray.copy(alpha = 0.2f))
                        Text(
                            text = " ATAU ",
                            style = Typography.labelSmall.copy(color = Gray),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Gray.copy(alpha = 0.2f))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        modifier = Modifier.clickable {
                            viewModel.setIsGuest(true)
                            navController.navigate(HOME_SCREEN)
                        },
                        text = "Lanjutkan sebagai Tamu",
                        style = Typography.bodyMedium.copy(color = bluePrimary, fontWeight = FontWeight.Medium)
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
                    text = "Belum punya akun? ",
                    style = Typography.bodyMedium
                )
                Text(
                    text = "Daftar",
                    style = Typography.bodyMedium.copy(
                        color = bluePrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable {
                        navController.navigate(REGISTER_SCREEN)
                    }
                )
            }
        }
    }
}
