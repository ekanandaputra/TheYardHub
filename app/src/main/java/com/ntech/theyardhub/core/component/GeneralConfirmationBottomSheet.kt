package com.ntech.theyardhub.core.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Gray
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralConfirmationBottomSheet(
    onDismiss: () -> Unit,
    header: String,
    title: String,
    description: String,
    onPrimaryClicked: () -> Unit,
    onSecondaryClicked: () -> Unit,
    labelSecondary: String = "Tolak",
    labelPrimary: String = "Konfirmasi",
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        containerColor = White,
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = header, style = Typography.titleLarge.copy(color = Black))
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                modifier = Modifier.size(152.dp),
                painter = painterResource(R.drawable.ic_general_not_found),
                contentDescription = "avatar",
                contentScale = ContentScale.FillBounds
            )
            Text(text = title, style = Typography.bodyLarge.copy(color = Black))
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = description, style = Typography.bodyMedium.copy(color = Gray))
            Spacer(modifier = Modifier.height(20.dp))
//            GeneralDoubleButton(
//                modifier = Modifier,
//                onSecondaryClicked = { onSecondaryClicked.invoke() },
//                onPrimaryClicked = { onPrimaryClicked.invoke() },
//                labelSecondary = labelSecondary,
//                labelPrimary = labelPrimary,
//            )
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GeneralConfirmationBottomSheetPreview() {
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "header", style = Typography.titleLarge.copy(color = Black))
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            modifier = Modifier.size(152.dp),
            painter = painterResource(R.drawable.ic_general_not_found),
            contentDescription = "avatar",
            contentScale = ContentScale.FillBounds
        )
        Text(text = "title", style = Typography.bodyLarge.copy(color = Black))
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "description", style = Typography.bodyMedium.copy(color = Gray))
        Spacer(modifier = Modifier.height(20.dp))
//        GeneralDoubleButton(
//            modifier = Modifier,
//            onSecondaryClicked = {},
//            onPrimaryClicked = {},
//            labelSecondary = "Batalkan",
//            labelPrimary = "Konfirm"
//        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}
