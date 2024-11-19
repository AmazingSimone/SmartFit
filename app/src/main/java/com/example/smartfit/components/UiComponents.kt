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
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.smartfit.data.Training
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import com.mmk.kmpauth.uihelper.google.GoogleSignInButtonIconOnly
import dev.gitlive.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


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
    value: String = "",
    defaultText: String = "",
    onTextChanged: (String) -> Unit,
    trailingIcon: @Composable() (() -> Unit)? = null,
    suffix: @Composable() (() -> Unit)? = null,
    keyBoardType: KeyboardType = KeyboardType.Text,
    enterButtonAction: ImeAction = ImeAction.Default,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorText: String = "",
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1
) {

    val textValue = remember {
        mutableStateOf(defaultText)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(currentFocusRequester),
        value = if (value.isNotEmpty()) value else textValue.value,
        onValueChange = {
            textValue.value = it
            onTextChanged(it)
        },
        label = { Text(text = label) },
        readOnly = readOnly,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        trailingIcon = {

            if (trailingIcon != null) {
                trailingIcon()
            } else if (textValue.value.isNotEmpty()) {
                IconButton(
                    onClick = {
                        textValue.value = ""
                        onTextChanged("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear input icon"
                    )
                }
            }


        },
        suffix = suffix,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyBoardType,
            imeAction = enterButtonAction
        ),
        keyboardActions = KeyboardActions(
            onNext = onNext
        ),
        visualTransformation = visualTransformation,
        supportingText = { Text(text = errorText) },
        isError = isError,

        )
}

@Composable
fun CustomPasswordOutlineInput(
    currentFocusRequester: FocusRequester,
    label: String,
    onPasswordChange: (String) -> Unit
) {

    val password = remember {
        mutableStateOf("")
    }
    val passwordVisible = remember {
        mutableStateOf(false)
    }

    CustomOutlinedTextInput(
        currentFocusRequester = currentFocusRequester,
        label = label,
        onTextChanged = {
            password.value = it
            onPasswordChange(it)
        },
        trailingIcon = {
            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = iconImage, contentDescription = "Password visibility")
            }
        },
        visualTransformation = if (!passwordVisible.value) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun CustomDateOutlineInput(
    currentFocusRequester: FocusRequester,
    defaultDate: Long = 0L,
    label: String,
    onDateChanged: (Long?) -> Unit
) {
    var selectedDate by remember { mutableStateOf<Long?>(defaultDate) }
    var showModal by remember { mutableStateOf(false) }
    CustomOutlinedTextInput(
        currentFocusRequester = currentFocusRequester,
        //defaultText = "",
        value = if (defaultDate == 0L) "" else selectedDate?.let { convertMillisToDate(it) }
            ?: convertMillisToDate(defaultDate),
        label = label,
        readOnly = true,
        trailingIcon = {
            IconButton(
                onClick = {
                    showModal = true
                }
            ) {
                Icon(Icons.Default.DateRange, contentDescription = "Select date")
            }
        },
        onTextChanged = { }
    )
    if (showModal) {
        DatePickerModal(
            onDateSelected = {
                selectedDate = it
                onDateChanged(it)
            },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("d.M.yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
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
fun CustomOneTapUiButtonWithFirebaseAuth(
    modifier: Modifier = Modifier,
    onFirebaseResult: (Result<FirebaseUser?>) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        //Google Sign-In Button and authentication with Firebase
        GoogleButtonUiContainerFirebase(onResult = onFirebaseResult, linkAccount = false) {
            GoogleSignInButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                fontSize = 19.sp
            ) { this.onClick() }
        }
    }
}

@Composable
fun CustomIconOnlyButtonAndFirebaseAuth(
    modifier: Modifier = Modifier,
    onFirebaseResult: (Result<FirebaseUser?>) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {

        //Google Sign-In IconOnly Button and authentication with Firebase
        GoogleButtonUiContainerFirebase(onResult = onFirebaseResult, linkAccount = false) {
            GoogleSignInButtonIconOnly(onClick = { this.onClick() })
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
    defaultPosition: Boolean = false,
    onSwitchChange: (Boolean) -> Unit,
    textColor: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        val switchedState = remember { mutableStateOf(defaultPosition) }

        Text(
            text = text,
            color = textColor,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        )

        Switch(
            checked = switchedState.value,
            onCheckedChange = {
                switchedState.value = it
                onSwitchChange(it)
            },
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
    size: Dp = 16.dp
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

@Composable
fun CustomProfilePictureFrame(
    pictureUrl: String,
    frameColor: Color = Color.Unspecified,
    enabled: Boolean = true,
    editOption: Boolean = false,
    onClick: () -> Unit = {},
    frameSize: Dp = 50.dp,
    profilePicRatio: Float = 0.80f
) {

    val color = if (frameColor == Color(0)) MaterialTheme.colorScheme.primary else frameColor

    Box(
        modifier = Modifier.clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.TopEnd
    ) {

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(frameSize)
                .background(color),
            contentAlignment = Alignment.Center
        ) {

            AsyncImage(
                model = pictureUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(frameSize * profilePicRatio)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
//            Box(
//                modifier = Modifier
//                    .clip(CircleShape)
//                    .size(frameSize * profilePicRatio)
//                    .background(Color.Gray)
//            )
        }
        if (editOption) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(frameSize * (profilePicRatio / 2.3f))
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Icon for edit profile option",
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size((frameSize * (profilePicRatio / 2.6f)) * profilePicRatio)
                )
            }
        }
    }
}

@Composable
fun CustomProfileInfoTable(
    avgTimeOfActivity: String,
    avgCountOfSteps: Int,
    avgBurnedCalories: Int,
    favouriteTraining: String
) {

    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Heading3("Priem. doba aktivity")
            HorizontalDivider(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            Heading3(avgTimeOfActivity)
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Heading3("Priem. pocet krokov")
            HorizontalDivider(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            Heading3(avgCountOfSteps.toString())
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Heading3("Priem. pocet spal. kalorii")
            HorizontalDivider(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            Heading3(avgBurnedCalories.toString())
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Heading3("Oblub. trening")
            HorizontalDivider(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            Heading3(favouriteTraining)
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
        Icon(
            imageVector = icon,
            contentDescription = "Icon Of Button",
            modifier = Modifier.size(size - 10.dp)
        )
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomTrainingInfoCardWithDate(
    modifier: Modifier = Modifier,
    training: Training,
    //timeLength: LocalTime = LocalTime.of(1, 20, 34),
    numberOfParticipants: Int = 0
) {

    Column(

    ) {

        Heading2(
            LocalDateTime.ofEpochSecond(training.timeDateOfTraining, 0, ZoneOffset.UTC)
                .format(
                    DateTimeFormatter.ofPattern(
                        if (LocalDateTime.now().year != LocalDateTime.ofEpochSecond(
                                training.timeDateOfTraining,
                                0,
                                ZoneOffset.UTC
                            ).year
                        ) "E, d.M.yyyy" else "E, d.M"
                    )
                )
        )
        Spacer(modifier = Modifier.height(8.dp)) // Add spacing here

        OutlinedCard(
            modifier = modifier.height(150.dp),
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
                            imageVector = training.icon,
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
                                    .weight(1.2f),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically,

                                ) {
                                Heading1(
                                    training.name,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                //if (goal > 0) HeadlineText(" / ${goal.toString()}", fontWeight = FontWeight.Bold)

                            }

                            if (numberOfParticipants > 1) NormalText(
                                "$numberOfParticipants ucastnici",
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.6f),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Heading1(
                                    LocalTime.ofSecondOfDay((training.trainingDuration / 1000).toLong())
                                        .format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                                    //timeLength.toString(),
                                    color = MaterialTheme.colorScheme.tertiary
                                )

                            }
                            Heading2(
                                LocalDateTime.ofEpochSecond(
                                    training.timeDateOfTraining,
                                    0,
                                    ZoneOffset.UTC
                                ).format(DateTimeFormatter.ofPattern("HH:mm"))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTrainingInfoDisplayCard(
    modifier: Modifier = Modifier,
    title: String,
    timeData: String = "",
    data: Float = 0f,
    unit: String = ""
) {
    Card {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Heading1(title)

            Row {
                if (timeData != null) HeadlineText(
                    timeData.toString(),
                    color = MaterialTheme.colorScheme.primary
                ) else HeadlineText(data.toString(), color = MaterialTheme.colorScheme.primary)
                HeadlineText(unit, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewComponents() {
    CustomTrainingInfoCardWithDate(
        training = Training(
            name = "Bezecky trener",
            icon = Icons.AutoMirrored.Filled.DirectionsRun,
            //creatorId = "MZ6M79VA9zetdUHX4NtgRE6UDzx2",
            trainingDuration = 9142,
            timeDateOfTraining = 1731964032,
            avgSpeed = 0F,
            burnedCalories = 0F,
            avgHeartRate = 0,
            avgTempo = 0,
            steps = 0,
            trainingTemperature = 0
        )
    )
}

