package com.ntech.theyardhub.feature.yards

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.LocationModel
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.feature.post.PostItem
import com.ntech.theyardhub.feature.post.PostViewModel
import org.koin.androidx.compose.get

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YardScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val image: Painter = painterResource(id = R.drawable.icon)
                        Image(
                            painter = image,
                            contentDescription = "App Logo",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "Yard",
                            modifier = Modifier.padding(start = 16.dp),
                            style = Typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = White
                )
            )
        },
        containerColor = White,
    ) { innerPadding ->

        val itemList: List<YardModel> = generateYardModels()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                ),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    bottom = 80.dp, top = innerPadding.calculateTopPadding() + 24.dp,
                )
            ) {
                items(itemList.size) { item ->
                    YardCardItem(
                        item = itemList[item],
                        onClickItem = {}
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

fun generateYardModels(): List<YardModel> {
    val yardModels = mutableListOf<YardModel>()

    yardModels.add(
        YardModel(
            "Kebun Anggur Makmur",
            "",
            "Description for Yard 1",
            LocationModel("Jakarta", -6.2088, 106.8456),
            "uuid-1"
        )
    )
    yardModels.add(
        YardModel(
            "Kebun Anggur Sejahtera",
            "",
            "Description for Yard 2",
            LocationModel("Bandung", -6.9175, 107.6191),
            "uuid-2"
        )
    )
    yardModels.add(
        YardModel(
            "Kebun Anggur A",
            "",
            "Description for Yard 3",
            LocationModel("Surabaya", -7.2504, 112.7688),
            "uuid-3"
        )
    )
    yardModels.add(
        YardModel(
            "Kebun Anggur B",
            "",
            "Description for Yard 4",
            LocationModel("Yogyakarta", -7.7956, 110.3695),
            "uuid-4"
        )
    )
    yardModels.add(
        YardModel(
            "Kebun Anggur D",
            "",
            "Description for Yard 5",
            LocationModel("Semarang", -6.9667, 110.4167),
            "uuid-5"
        )
    )
    yardModels.add(
        YardModel(
            "Yard 6",
            "",
            "Description for Yard 6",
            LocationModel("Denpasar", -8.6500, 115.2167),
            "uuid-6"
        )
    )
    yardModels.add(
        YardModel(
            "Yard 7",
            "",
            "Description for Yard 7",
            LocationModel("Medan", 3.5952, 98.6722),
            "uuid-7"
        )
    )
    yardModels.add(
        YardModel(
            "Yard 8",
            "",
            "Description for Yard 8",
            LocationModel("Makassar", -5.1477, 119.4327),
            "uuid-8"
        )
    )
    yardModels.add(
        YardModel(
            "Yard 9",
            "",
            "Description for Yard 9",
            LocationModel("Palembang", -2.9909, 104.7565),
            "uuid-9"
        )
    )
    yardModels.add(
        YardModel(
            "Yard 10",
            "",
            "Description for Yard 10",
            LocationModel("Balikpapan", -1.2675, 116.8289),
            "uuid-10"
        )
    )

    return yardModels
}