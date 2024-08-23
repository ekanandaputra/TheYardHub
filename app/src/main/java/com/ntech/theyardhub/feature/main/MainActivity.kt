package com.ntech.theyardhub.feature.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ntech.theyardhub.core.RouteName.CHAT_SCREEN
import com.ntech.theyardhub.core.RouteName.DETAIL_POST_SCREEN
import com.ntech.theyardhub.core.RouteName.DETAIL_YARD_SCREEN
import com.ntech.theyardhub.core.RouteName.HOME_SCREEN
import com.ntech.theyardhub.core.RouteName.LOGIN_SCREEN
import com.ntech.theyardhub.core.RouteName.POST_SCREEN
import com.ntech.theyardhub.core.RouteName.PRODUCT_SCREEN
import com.ntech.theyardhub.core.RouteName.REGISTER_SCREEN
import com.ntech.theyardhub.core.component.RoundedEditField
import com.ntech.theyardhub.core.theme.TheYardHubTheme
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.utils.RupiahVisualTransformation
import com.ntech.theyardhub.feature.bottomnavigation.BottomNavigationMenu
import com.ntech.theyardhub.feature.login.LoginScreen
import com.ntech.theyardhub.feature.chat.ChatScreen
import com.ntech.theyardhub.feature.detailpost.DetailPostScreen
import com.ntech.theyardhub.feature.home.DetailYardScreen
import com.ntech.theyardhub.feature.home.HomeScreen
import com.ntech.theyardhub.feature.post.PostScreen
import com.ntech.theyardhub.feature.product.ProductScreen
import com.ntech.theyardhub.feature.register.RegisterScreen
import org.koin.androidx.compose.get

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel: MainActivityViewModel = get()

            TheYardHubTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = White
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    NavHost(
                        navController = navController,
                        startDestination = DETAIL_YARD_SCREEN
                    ) {
                        composable(LOGIN_SCREEN) {
                            LoginScreen(navController)
                        }
                        composable(HOME_SCREEN) {
                            HomeScreen(navController)
                        }
                        composable(CHAT_SCREEN) {
                            ChatScreen(navController)
                        }
                        composable(POST_SCREEN) {
                            PostScreen(navController)
                        }
                        composable(DETAIL_POST_SCREEN) {
                            DetailPostScreen(navController)
                        }
                        composable(PRODUCT_SCREEN) {
                            ProductScreen(navController)
                        }
                        composable(REGISTER_SCREEN) {
                            RegisterScreen(navController)
                        }
                        composable(DETAIL_YARD_SCREEN) {
                            DetailYardScreen(navController)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        if (currentRoute != DETAIL_YARD_SCREEN ) {
                            BottomNavigationMenu(
                                navController = navController,
                                selectedMenuState = viewModel.selectedMenuState.value,
                                onItemClicked = { viewModel.setSelectedMenu(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}