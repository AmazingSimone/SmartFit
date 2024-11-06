package com.example.smartfit.Components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun NormalText(text: String, fontWeight: FontWeight? = null, textAlign: TextAlign? = TextAlign.Center) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun Heading1(text: String, fontWeight: FontWeight? = null, textAlign: TextAlign? = TextAlign.Center) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun Heading2(text: String, fontWeight: FontWeight? = null, textAlign: TextAlign? = TextAlign.Center) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun Heading3(text: String, fontWeight: FontWeight? = null, textAlign: TextAlign? = TextAlign.Center) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontSize = MaterialTheme.typography.titleSmall.fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun HeadlineText(text: String, fontWeight: FontWeight? = null, textAlign: TextAlign? = TextAlign.Center) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun CustomOutlinedTextInput(
    currentFocusRequester: FocusRequester,
    onNext: (KeyboardActionScope.() -> Unit)? = null,
    label: String,
    trailingIcon: ImageVector,
    keyBoardType: KeyboardType,
    enterButtonAction: ImeAction,
    isPassword: Boolean = false,
    errorText: String = "",
    isError: Boolean = false
) {
    val inputText = remember { mutableStateOf("") }
    val passwordVisible = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(currentFocusRequester),
            //.width(250.dp),
        value = inputText.value,
        onValueChange = { inputText.value = it },
        label = { Text(text = label) },
        singleLine = true,
        trailingIcon = {

            if (isPassword) {
                val iconImage = if (passwordVisible.value) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }

                IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                    Icon(imageVector = iconImage, contentDescription = "Password visibility")
                }
            } else {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = "Trailing Icon"
                )
            }

        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyBoardType,
            imeAction = enterButtonAction
        ),
        keyboardActions = KeyboardActions(
            onNext = onNext
        ),
        visualTransformation = if (!passwordVisible.value && isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        supportingText = { Text(text = errorText) },
        isError = isError
    )
}

@Composable
fun CustomButton(
    onClick: () -> Unit,
    outlined: Boolean = false,
    buttonText: String,
    enabled: Boolean = true,
    textColor: Color =  ButtonDefaults.buttonColors().contentColor,
    containerColor: Color = ButtonDefaults.buttonColors().containerColor,
    modifier: Modifier = Modifier
) {
    if (outlined) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            enabled = enabled,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = containerColor)
        ) {
            Text(text = buttonText)
        }
    } else {
        Button(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = textColor)
        ) {
            Text(text = buttonText)
        }
    }
}
