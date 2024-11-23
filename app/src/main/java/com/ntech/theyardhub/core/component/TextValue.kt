package com.ntech.theyardhub.core.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ntech.theyardhub.core.theme.Typography

@Composable
fun TextValue(label: String, value: String) {
    Row {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = Typography.labelMedium
        )
        Text(text = ":", style = Typography.labelMedium)
        Text(
            text = value, modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            style = Typography.labelMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextValuePreview() {
    TextValue(label = "Label", value = "Value")
}