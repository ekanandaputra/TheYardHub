package com.ntech.theyardhub.feature.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ntech.weedwhiz.feature.bottomnavigation.BottomNavItem

class MainActivityViewModel() : ViewModel() {
    val selectedMenuState = mutableStateOf<BottomNavItem>(BottomNavItem.Home)

    fun setSelectedMenu(item: BottomNavItem) {
        selectedMenuState.value = item
    }
}