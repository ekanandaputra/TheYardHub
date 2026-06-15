package com.ntech.theyardhub.feature.yards

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.core.RouteName.DETAIL_YARD_SCREEN
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.component.ModernTextField
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.YardModel
import org.koin.androidx.compose.get

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YardScreen(navController: NavController) {
    val viewModel: YardViewModel = get()
    val yardState by viewModel.yardLiveData.observeAsState()
    
    var selectedCity by remember { mutableStateOf("Semua") }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    
    var itemsList by remember { mutableStateOf(listOf<YardModel>()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchYards()
    }

    when (val state = yardState) {
        is AppResponse.Loading -> isLoading = true
        is AppResponse.Success -> {
            isLoading = false
            itemsList = state.data
        }
        else -> isLoading = false
    }

    val cities = remember(itemsList) {
        val list = mutableListOf("Semua")
        // In real app, we might get unique cities from the current list or a separate API
        // For now, let's just use some common cities or unique ones from the list
        val uniqueCities = itemsList.map { it.locationModel.city }.filter { it.isNotEmpty() }.distinct()
        list.addAll(uniqueCities)
        list
    }

    if (isLoading) LoadingDialog {}

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Daftar Lahan",
                        style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Bar
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                ModernTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.searchYards(it.text)
                    },
                    label = "Cari Lahan",
                    hint = "Masukkan nama atau kota..."
                )
            }

            // Filter Cities
            if (cities.size > 1) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cities) { city ->
                        val isSelected = selectedCity == city
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) bluePrimary else bluePrimary.copy(alpha = 0.05f))
                                .clickable {
                                    selectedCity = city
                                    viewModel.filterYards(city)
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = city,
                                style = Typography.labelLarge.copy(
                                    color = if (isSelected) White else bluePrimary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }

            // Yard List
            if (itemsList.isEmpty() && !isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tidak ada lahan yang ditemukan", style = Typography.bodyMedium)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(itemsList) { yard ->
                        YardCardItem(
                            item = yard,
                            onClickItem = {
                                navController.navigate("$DETAIL_YARD_SCREEN/${yard.documentId}")
                            }
                        )
                    }
                }
            }
        }
    }
}
