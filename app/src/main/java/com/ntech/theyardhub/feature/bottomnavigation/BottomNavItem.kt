package com.ntech.theyardhub.feature.bottomnavigation

import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.RouteName

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {

    object Home : BottomNavItem("Home", R.drawable.icon, RouteName.HOME_SCREEN)
    object Article : BottomNavItem("Article", R.drawable.icon, RouteName.POST_SCREEN)
    object Yard : BottomNavItem("Farms", R.drawable.icon, RouteName.YARD_SCREEN)
    object Profile : BottomNavItem("Profile", R.drawable.icon, RouteName.DETAIL_USER_SCREEN)

}