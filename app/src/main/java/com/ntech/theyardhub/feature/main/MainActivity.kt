package com.ntech.theyardhub.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ntech.theyardhub.core.RouteName.CHAT_LIST_SCREEN
import com.ntech.theyardhub.core.RouteName.CHAT_SCREEN
import com.ntech.theyardhub.core.RouteName.CREATE_PRODUCT_SCREEN
import com.ntech.theyardhub.core.RouteName.DETAIL_POST_SCREEN
import com.ntech.theyardhub.core.RouteName.DETAIL_PRODUCT_SCREEN
import com.ntech.theyardhub.core.RouteName.DETAIL_USER_SCREEN
import com.ntech.theyardhub.core.RouteName.DETAIL_YARD_SCREEN
import com.ntech.theyardhub.core.RouteName.HOME_SCREEN
import com.ntech.theyardhub.core.RouteName.LOGIN_SCREEN
import com.ntech.theyardhub.core.RouteName.POST_SCREEN
import com.ntech.theyardhub.core.RouteName.PRODUCT_SCREEN
import com.ntech.theyardhub.core.RouteName.REGISTER_SCREEN
import com.ntech.theyardhub.core.RouteName.REGISTER_YARD_SCREEN
import com.ntech.theyardhub.core.RouteName.SPLASH_SCREEN
import com.ntech.theyardhub.core.RouteName.YARD_SCREEN
import com.ntech.theyardhub.core.theme.TheYardHubTheme
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.feature.bottomnavigation.BottomNavigationMenu
import com.ntech.theyardhub.feature.chat.ChatScreen
import com.ntech.theyardhub.feature.chatlist.ChatListScreen
import com.ntech.theyardhub.feature.createproduct.CreateProductScreen
import com.ntech.theyardhub.feature.detailpost.DetailPostScreen
import com.ntech.theyardhub.feature.detailproduct.DetailProductScreen
import com.ntech.theyardhub.feature.detailuser.DetailUserScreen
import com.ntech.theyardhub.feature.detailyard.DetailYardScreen
import com.ntech.theyardhub.feature.home.HomeScreen
import com.ntech.theyardhub.feature.login.LoginScreen
import com.ntech.theyardhub.feature.post.PostScreen
import com.ntech.theyardhub.feature.product.ProductScreen
import com.ntech.theyardhub.feature.register.RegisterScreen
import com.ntech.theyardhub.feature.registeryard.RegisterYardScreen
import com.ntech.theyardhub.feature.splash.SplashScreen
import com.ntech.theyardhub.feature.yards.YardScreen
import org.koin.androidx.compose.get

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {

            val viewModel: MainActivityViewModel = get()

            TheYardHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = White
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    NavHost(
                        navController = navController, startDestination = SPLASH_SCREEN
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
                        composable(
                            route = "$DETAIL_POST_SCREEN/{postId}",
                            arguments = listOf(navArgument("postId") {
                                NavType.StringType.also {
                                    type = it
                                }
                            })
                        ) { backStackEntry ->
                            val postId = backStackEntry.arguments?.getString("postId")
                            DetailPostScreen(navController, postId ?: "")
                        }
                        composable(PRODUCT_SCREEN) {
                            ProductScreen(navController)
                        }
                        composable(REGISTER_SCREEN) {
                            RegisterScreen(navController)
                        }
                        composable(
                            route = "$DETAIL_YARD_SCREEN/{yardId}",
                            arguments = listOf(navArgument("yardId") {
                                NavType.StringType.also {
                                    type = it
                                }
                            })
                        ) { backStackEntry ->
                            val yardId = backStackEntry.arguments?.getString("yardId")
                            DetailYardScreen(navController, yardId ?: "")
                        }
                        composable(DETAIL_USER_SCREEN) {
                            DetailUserScreen(navController)
                        }
                        composable(SPLASH_SCREEN) {
                            SplashScreen(navController)
                        }
                        composable(YARD_SCREEN) {
                            YardScreen(navController)
                        }
                        composable(DETAIL_PRODUCT_SCREEN) {
                            DetailProductScreen(navController)
                        }
                        composable(CREATE_PRODUCT_SCREEN) {
                            CreateProductScreen(navController)
                        }
                        composable(CHAT_LIST_SCREEN) {
                            ChatListScreen(navController)
                        }
                        composable(REGISTER_YARD_SCREEN) {
                            RegisterYardScreen(navController)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        if (currentRoute == HOME_SCREEN ||
                            currentRoute == POST_SCREEN ||
                            currentRoute == YARD_SCREEN ||
                            currentRoute == DETAIL_USER_SCREEN
                        ) {
                            BottomNavigationMenu(navController = navController,
                                selectedMenuState = viewModel.selectedMenuState.value,
                                onItemClicked = { viewModel.setSelectedMenu(it) })
                        }
                    }
                }
            }
        }
    }
}