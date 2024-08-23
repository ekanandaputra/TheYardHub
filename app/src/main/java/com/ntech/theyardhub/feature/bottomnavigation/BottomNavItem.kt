package com.ntech.weedwhiz.feature.bottomnavigation

import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.RouteName

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {

    object Home : BottomNavItem("Home", R.drawable.icon, RouteName.HOME_SCREEN)
    object Article : BottomNavItem("Article", R.drawable.icon, RouteName.POST_SCREEN)
    object Garden : BottomNavItem("Garden", R.drawable.icon, RouteName.GARDEN_SCREEN)

}