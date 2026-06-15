package com.ntech.theyardhub.feature.bottomnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.ntech.theyardhub.core.RouteName

sealed class BottomNavItem(var title: String, var icon: ImageVector, var screen_route: String) {

    object Home : BottomNavItem("Beranda", Icons.Default.Home, RouteName.HOME_SCREEN)
    object Article : BottomNavItem("Artikel", Icons.AutoMirrored.Filled.Article, RouteName.POST_SCREEN)
    object Yard : BottomNavItem("Lahan", Icons.Default.Agriculture, RouteName.YARD_SCREEN)
    object Profile : BottomNavItem("Profil", Icons.Default.Person, RouteName.DETAIL_USER_SCREEN)

}