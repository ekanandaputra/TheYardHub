package com.ntech.theyardhub.feature.registeryard

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.component.RoundedEditField
import com.ntech.theyardhub.core.component.SuccessDialog
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterYardScreen(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()
    val mContext = LocalContext.current
    val viewModel: RegisterYardViewModel = get()

    val nameState by viewModel.nameState
    val descriptionState by viewModel.descriptionState
    val locationState by viewModel.locationState

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchDetailUser()
    }

    Configuration.getInstance().userAgentValue = mContext.packageName

    val showDialog = remember { mutableStateOf(false) }
    val showSuccessDialog = remember { mutableStateOf(false) }

    var data = UserModel()

    when (val userState = viewModel.userLiveData.observeAsState().value) {
        is AppResponse.Loading -> {
            showDialog.value = true
        }

        is AppResponse.Empty -> {
            showDialog.value = false
        }

        is AppResponse.Success -> {
            showDialog.value = false
            data = userState.data
            if (data.yard.thumbnail.isNotEmpty()) {
                imageUri = Uri.parse(data.yard.thumbnail)
            }
            viewModel.setName(TextFieldValue(data.yard.name))
            viewModel.setDescription(TextFieldValue(data.yard.description))
        }

        else -> {
            showDialog.value = false
        }
    }

    when (val updateState = viewModel.updateYardLiveData.observeAsState().value) {
        is AppResponse.Loading -> {
            showDialog.value = true
        }

        is AppResponse.Success -> {
            showDialog.value = false
            showSuccessDialog.value = true
        }

        is AppResponse.Error -> {
            showDialog.value = false
            Toast.makeText(mContext, updateState.message, Toast.LENGTH_SHORT).show()
        }

        else -> {
            showDialog.value = false
        }
    }

    if (showDialog.value) LoadingDialog(setShowDialog = {})

    if (showSuccessDialog.value) {
        SuccessDialog(
            setShowDialog = { showSuccessDialog.value = it },
            title = "Farm Registered!",
            message = "Your farm data has been successfully saved to the hub.",
            onDismiss = {
                navController.popBackStack()
            }
        )
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register Farm") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                GeneralButton(
                    onButtonClicked = {
                        coroutineScope.launch {
                            viewModel.updateYard(imageUri)
                        }
                    },
                    label = "Register",
                    buttonType = ButtonType.PRIMARY,
                    buttonHeight = ButtonHeight.MEDIUM,
                    isEnabled = true,
                )
            }
        },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding() + 24.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .clickable {
                        galleryLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Image from URL",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(text = "Tap to select an image", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            RoundedEditField(
                title = "Name",
                value = nameState,
                onValueChange = { value ->
                    viewModel.setName(value)
                },
                hint = "Name"
            )
            Spacer(modifier = Modifier.height(16.dp))
            RoundedEditField(
                title = "Description",
                value = descriptionState,
                onValueChange = { value ->
                    viewModel.setDescription(value)
                },
                hint = "Enter Description",
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Farm Location",
                style = Typography.titleMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                AndroidView(
                    factory = { context ->
                        MapView(context).apply {
                            setTileSource(TileSourceFactory.MAPNIK)
                            setMultiTouchControls(true)
                            controller.setZoom(15.0)
                            val startPoint = GeoPoint(locationState.latitude, locationState.longitude)
                            controller.setCenter(startPoint)

                            val marker = Marker(this)
                            marker.position = startPoint
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            marker.title = "Farm Location"
                            overlays.add(marker)

                            val mapEventsReceiver = object : MapEventsReceiver {
                                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                                    p?.let {
                                        viewModel.setLocation(it.latitude, it.longitude)
                                        marker.position = it
                                        invalidate()
                                    }
                                    return true
                                }

                                override fun longPressHelper(p: GeoPoint?): Boolean = false
                            }
                            overlays.add(MapEventsOverlay(mapEventsReceiver))
                        }
                    },
                    update = { mapView ->
                        val point = GeoPoint(locationState.latitude, locationState.longitude)
                        mapView.controller.animateTo(point)
                        val marker = mapView.overlays.filterIsInstance<Marker>().firstOrNull()
                        marker?.position = point
                        mapView.invalidate()
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

