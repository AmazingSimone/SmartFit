package com.example.smartfit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun NormalText(
    text: String,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = TextAlign.Center
) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun Heading1(
    text: String,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = TextAlign.Center
) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun Heading2(
    text: String,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = TextAlign.Center
) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun Heading3(
    text: String,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = TextAlign.Center
) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontSize = MaterialTheme.typography.titleSmall.fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun HeadlineText(
    text: String,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = TextAlign.Center
) {
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
    isError: Boolean = false,
    errorText: String = ""
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    outlined: Boolean = false,
    buttonText: String,
    enabled: Boolean = true,
    textColor: Color = ButtonDefaults.buttonColors().contentColor,
    containerColor: Color = ButtonDefaults.buttonColors().containerColor,
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
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = textColor
            )
        ) {
            Text(text = buttonText)
        }
    }
}

@Composable
fun CustomTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonText: String,
    enabled: Boolean = true,
    textColor: Color = ButtonDefaults.textButtonColors().contentColor
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        colors = ButtonDefaults.elevatedButtonColors(contentColor = textColor)
    ) {
        Text(text = buttonText)
    }
}

@Composable
fun CustomCheckBox(
    text: String,
    textColor: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        val checkedState = remember { mutableStateOf(false) }

        Text(
            text = text,
            color = textColor,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        )

        Checkbox(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it },
            enabled = enabled
        )
    }
}

@Composable
fun CustomSwitch(
    text: String,
    textColor: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        val switchedState = remember { mutableStateOf(false) }

        Text(
            text = text,
            color = textColor,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        )

        Switch(
            checked = switchedState.value,
            onCheckedChange = { switchedState.value = it },
            enabled = enabled
        )

    }
}

@Composable
fun CustomLargeIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    iconSize: Dp = 200.dp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(iconSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f))
                .clickable(onClick = onClick)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Icon of button",
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun CustomOnlineStateIndicator(
    icon: ImageVector = Icons.Filled.Watch,
    text: String = "",
    indicatorColor: Color = MaterialTheme.colorScheme.error,
    onClick: () -> Unit,
    size: Dp = 23.dp
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(25.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            if (text.isNotEmpty()) {
                Text(
                    text = text,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Bold

                )
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = "Icon of indicator",
                    modifier = Modifier.size(size)
                )
            }

            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(indicatorColor)
            )
        }
    }
}
