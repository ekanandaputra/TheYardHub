package com.ntech.theyardhub.feature.createproduct

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ntech.theyardhub.core.ButtonHeight
import com.ntech.theyardhub.core.ButtonType
import com.ntech.theyardhub.core.component.GeneralButton
import com.ntech.theyardhub.core.component.LoadingDialog
import com.ntech.theyardhub.core.component.RoundedEditField
import com.ntech.theyardhub.core.component.SuccessDialog
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ProductModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductScreen(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()
    val mContext = LocalContext.current
    val viewModel: CreateProductViewModel = get()

    val nameState by viewModel.nameState
    val descriptionState by viewModel.descriptionState
    val priceState by viewModel.priceState
    var selectedImages by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    val showDialog = remember { mutableStateOf(false) }
    val showSuccessDialog = remember { mutableStateOf(false) }

    when (val createProductState = viewModel.createProductLiveData.observeAsState().value) {
        is AppResponse.Loading -> {
            showDialog.value = true
        }

        is AppResponse.Success -> {
            showDialog.value = false
            showSuccessDialog.value = true
        }

        is AppResponse.Error -> {
            showDialog.value = false
            Toast.makeText(mContext, createProductState.message, Toast.LENGTH_SHORT).show()
        }

        else -> {
            showDialog.value = false
        }
    }

    if (showDialog.value) LoadingDialog(setShowDialog = {})

    if (showSuccessDialog.value) {
        SuccessDialog(
            setShowDialog = { showSuccessDialog.value = it },
            title = "Product Created!",
            message = "Your product has been successfully added to your farm.",
            onDismiss = {
                navController.popBackStack()
            }
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            selectedImages = uris
        }
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Product") },
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
                            if (selectedImages.isNotEmpty()) {
                                viewModel.uploadImagesAndCreateProduct(selectedImages)
                            } else {
                                Toast.makeText(mContext, "Please select at least one image", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    label = "Create Product",
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
            if (selectedImages.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedImages.size) { index ->
                        AsyncImage(
                            model = selectedImages[index],
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .width(200.dp)
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { galleryLauncher.launch("image/*") },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            } else {
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
                    Text(text = "Tap to select images", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            RoundedEditField(
                title = "Name",
                value = nameState,
                onValueChange = { value ->
                    viewModel.setName(value)
                },
                hint = "Enter Product Name"
            )
            Spacer(modifier = Modifier.height(16.dp))
            RoundedEditField(
                title = "Description",
                value = descriptionState,
                onValueChange = { value ->
                    viewModel.setDescription(value)
                },
                hint = "Enter Description"
            )
            Spacer(modifier = Modifier.height(16.dp))
            RoundedEditField(
                title = "Price",
                value = priceState,
                onValueChange = { value ->
                    viewModel.setPrice(value)
                },
                hint = "Enter Price",
            )
        }
    }
}

