package com.ntech.theyardhub.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Gray
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White

@Composable
fun RoundedEditField(
    modifier: Modifier = Modifier,
    title: String = "",
    titleColor: Color = Black,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    cornerRadius: Dp = 15.dp,
    textColor: Color = Black,
    hint: String,
    isReadOnly: Boolean = false,
    onClickField: () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column {
        val modifierEditText = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(White, RoundedCornerShape(cornerRadius))
            .border(1.dp, Gray, RoundedCornerShape(cornerRadius))
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        if (isReadOnly) onClickField.invoke()
                    }
                }
            }

        if (title != "") {
            Text(title, style = Typography.labelMedium.copy(color = titleColor))
            modifierEditText.padding(top = 10.dp)
        } else {
            modifierEditText.padding(top = 0.dp)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = modifierEditText,
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center),
                value = value,
                onValueChange = onValueChange,
                visualTransformation = visualTransformation,
                textStyle = Typography.titleMedium.copy(color = textColor),
                decorationBox = { innerTextField ->
                    if (value.text.isEmpty()) {
                        Text(
                            text = hint,
                            color = Gray
                        )
                    }
                    innerTextField()
                },
                readOnly = isReadOnly,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedTextFieldDefault() {
    RoundedEditField(
        title = "{title}",
        value = TextFieldValue(),
        onValueChange = {},
        hint = "",
        isReadOnly = true,
        onClickField = {}
    )
}