package com.example.smartfit.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun NormalText(
    text: String,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = TextAlign.Center,
) {
    Text(
        text = text,
        color = color,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun Heading1(
    text: String,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = TextAlign.Center,
) {
    Text(
        text = text,
        color = color,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun Heading2(
    text: String,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = TextAlign.Center,

    ) {
    Text(
        text = text,
        color = color,
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun Heading3(
    text: String,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = TextAlign.Center
) {
    Text(
        text = text,
        color = color,
        fontSize = MaterialTheme.typography.titleSmall.fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun HeadlineText(
    text: String,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = TextAlign.Center
) {
    Text(
        text = text,
        color = color,
        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
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

//TODO - Tu treba samozrejme dorobit cestu k fotke a veci s tym...
@Composable
fun CustomProfilePictureFrame(
    frameColor: Color = Color.Magenta,
    enabled: Boolean = true,
    editOption: Boolean = false,
    onClick: () -> Unit = {},
    frameSize: Dp = 50.dp,
    profilePicRatio: Float = 0.80f
) {

    Box(
        modifier = Modifier.clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.TopEnd
    ) {

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(frameSize)
                .background(frameColor),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(frameSize * profilePicRatio)
                    .background(Color.Gray)
            )
        }
        if (editOption) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(frameSize * (profilePicRatio / 2.3f))
                    .background(frameColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Icon for edit profile option",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size((frameSize * (profilePicRatio / 2.6f)) * profilePicRatio)
                )
            }
        }
    }
}

@Composable
fun CustomIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    size: Dp = 50.dp,
    enabled: Boolean = true,
    color: IconButtonColors = IconButtonDefaults.iconButtonColors()
) {

    IconButton(
        onClick = onClick,
        modifier = Modifier.size(size),
        enabled = enabled,
        colors = color
    ) {
        Icon(imageVector = icon, contentDescription = "Icon Of Button", modifier = Modifier.size(size - 10.dp))
    }
}


//TODO - este niesom velmi isty na 100% s designom plus by som farby mohol nastavit podla typu
// nameranych dat nejake urcit...
@Composable
fun CustomInfoCardFromDevice(
    heading: String,
    data: Int,
    goal: Int = 0,
    unit: String = "",
    icon: ImageVector
) {
    ElevatedCard(
        modifier = Modifier.height(150.dp)
    ) {
        Box {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Heading1(heading)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HeadlineText(data.toString(), fontWeight = FontWeight.Bold)
                        if (goal > 0) HeadlineText(
                            " / ${goal}",
                            fontWeight = FontWeight.Bold
                        )
                        HeadlineText(unit, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "Device info card icon",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

//TODO - treba daky enum na treningy spravit a pozor na anotacie
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomTrainingInfoCardWithDate(
    trainingType: String,
    timeLength: LocalTime = LocalTime.of(1, 20, 34),
    timeOfTraining: LocalTime = LocalTime.now(),
    numberOfParticipants: Int = 0,
    date: LocalDate = LocalDate.now(),
    trainingIcon: ImageVector
) {

    Column(

    ) {

        Heading2(date.format(DateTimeFormatter.ofPattern("E, d.M")))
        Spacer(modifier = Modifier.height(8.dp)) // Add spacing here

        OutlinedCard(
            modifier = Modifier.height(150.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            Box {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (numberOfParticipants > 1) Heading1("Skupinovy trening") else Heading1("Trening")

                        Icon(
                            imageVector = trainingIcon,
                            contentDescription = "Device info card icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,

                                ) {
                                HeadlineText(
                                    trainingType,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                //if (goal > 0) HeadlineText(" / ${goal.toString()}", fontWeight = FontWeight.Bold)
                                HeadlineText(
                                    timeLength.toString(),
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }

                            if (numberOfParticipants > 1) NormalText(
                                "$numberOfParticipants ucastnici",
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Column {


                                Heading1(timeOfTraining.format(DateTimeFormatter.ofPattern("HH:mm")))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTrainingInfoDisplayCard(
    modifier: Modifier = Modifier.padding(40.dp),
    title: String,
    timeData: LocalTime? = null,
    data: Float = 0f,
    unit: String = ""
) {
    Card {
        Column (
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Heading1(title)

            Row {
                if (timeData != null) HeadlineText(timeData.toString(), color = MaterialTheme.colorScheme.primary) else HeadlineText(data.toString(), color = MaterialTheme.colorScheme.primary)
                HeadlineText(unit, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun Preview() {
    CustomTrainingInfoDisplayCard(
        title = "Vzdialenost",
        data = 1.1f,
        unit = "km"
    )
}

