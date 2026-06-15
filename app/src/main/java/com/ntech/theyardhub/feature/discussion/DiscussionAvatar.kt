package com.ntech.theyardhub.feature.discussion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary

@Composable
fun DiscussionAvatar(
    name: String,
    modifier: Modifier = Modifier
) {
    val initial = if (name.isNotEmpty()) name.take(1).uppercase() else "?"
    
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(bluePrimary.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = androidx.compose.ui.text.TextStyle(
                color = bluePrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        )
    }
}
