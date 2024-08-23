package com.ntech.theyardhub.core.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ntech.theyardhub.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LoadImageWithGlide(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        GlideImage(
            contentScale = ContentScale.FillBounds,
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = modifier
        )
    }
}

@Composable
fun RoundedImageExample() {
    val imageModifier = Modifier
        .size(40.dp) // Set the size of the image
        .clip(CircleShape) // Clip the image to be round

    Image(
        painter = painterResource(id = R.drawable.icon),
        contentDescription = "Rounded Image",
        modifier = imageModifier,
        contentScale = ContentScale.Crop // Crop the image if necessary
    )
}