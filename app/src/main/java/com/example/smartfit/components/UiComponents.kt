package com.example.smartfit.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.example.smartfit.R
import com.example.smartfit.data.GroupTraining
import com.example.smartfit.data.NrfData
import com.example.smartfit.data.Training
import com.example.smartfit.data.User
import com.example.smartfit.data.frameColors
import com.example.smartfit.data.trainingList
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
    modifier: Modifier = Modifier,
    currentFocusRequester: FocusRequester = FocusRequester(),
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
    supportingText: @Composable() (() -> Unit)? = null,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1
) {

    val textValue = remember {
        mutableStateOf(defaultText)
    }

    OutlinedTextField(
        modifier = modifier
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
        supportingText = supportingText,
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
    enabled: Boolean = false,
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
fun ProfileInfoContent(
    padding: Dp = 8.dp,
    onEditClick: () -> Unit = {},
    onUnFollowButtonClick: (String) -> Unit,
    onFollowButtonClick: (String) -> Unit,
    recievedUser: User,
    followedUsersList: List<User>,
    completedtrainingsList: List<Training>,
    loggedInUser: User?,
    loggedInUserfollowedUsersList: List<User>,
    editOption: Boolean = false,
    enabled: Boolean = false
) {

    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CustomProfilePictureFrame(
            pictureUrl = recievedUser.profilePicUrl.toString(),
            editOption = editOption,
            enabled = enabled,
            frameColor = Color(frameColors[recievedUser.color]),
            frameSize = 200.dp,
            onClick = { onEditClick() }
        )
        Spacer(Modifier.padding(padding))

        HeadlineText(recievedUser.displayName ?: "")

        Spacer(Modifier.padding(padding - 6.dp))

        Row {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(8.dp)
            ) {
                Heading3("Trenigy ${completedtrainingsList.size}")
            }
            Spacer(Modifier.padding(padding))
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(8.dp)
            ) {
                Heading3("Sleduje ${followedUsersList.size}")
            }
        }

        if (recievedUser.bio.isNotEmpty()) {

            Spacer(Modifier.padding(padding - 6.dp))

            NormalText(recievedUser.bio ?: "")

        }

        Spacer(Modifier.padding(padding))

        if (loggedInUser != null) {
            if (recievedUser.id != loggedInUser.id) {

                if (loggedInUserfollowedUsersList.contains(recievedUser)) {

                    CustomButton(
                        onClick = { onUnFollowButtonClick(recievedUser.id) },
                        buttonText = "Sledujes"
                    )
                } else {
                    CustomButton(
                        onClick = { onFollowButtonClick(recievedUser.id) },
                        buttonText = "Zacat sledovat",
                        outlined = true
                    )
                }
                Spacer(Modifier.padding(padding))

            }
        }



        CustomProfileInfoTable(
            avgTimeOfActivity = "0min",
            avgCountOfSteps = 0,
            avgBurnedCalories = 0,
            favouriteTraining = "--"
        )


    }

}

@Composable
fun RunningTrainingInfoContent(
    stopWatch: StopWatch,
    nrfData: NrfData
) {

    Column(Modifier.fillMaxSize()) {
        CustomTrainingInfoDisplayCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            title = "Cas",
            data = stopWatch.getCustomFormattedTime()
        )
        Spacer(Modifier.padding(5.dp))
        Row(Modifier.weight(1f)) {
            CustomTrainingInfoDisplayCard(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                title = "Vzdialenost",
                data = nrfData.vzdialenost,
                unit = "m"
            )
            Spacer(Modifier.padding(5.dp))
            CustomTrainingInfoDisplayCard(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                title = "Srdcovy tep",
                data = nrfData.tep,
                unit = "t/m"
            )
        }
        //pocet krokov za minutu
        Spacer(Modifier.padding(5.dp))
        Row(Modifier.weight(1f)) {
            CustomTrainingInfoDisplayCard(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                title = "Kadencia",
                data = if (stopWatch.getTimeMillis() <= 0L) {
                    "0"
                } else {
                    val timeInSeconds = stopWatch.getTimeMillis() / 1000
                    if (timeInSeconds == 0L) {
                        "0"
                    } else {
                        ((nrfData.kroky.toInt() / timeInSeconds) * 60).toInt().toString()
                    }
                },
                unit = "kr/min"
            )

            Spacer(Modifier.padding(5.dp))
            CustomTrainingInfoDisplayCard(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                title = "Rychlost",
                data = ((nrfData.vzdialenost.replace(",", ".")
                    .toDouble() / (stopWatch.getTimeMillis() / 1000)) * 3.6).toInt().toString(),
                unit = "km/h"
            )
        }
        Spacer(Modifier.padding(5.dp))
        Row(Modifier.weight(1f)) {
            CustomTrainingInfoDisplayCard(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                title = "Spalene kalorie",
                data = nrfData.spaleneKalorie,
                unit = "kcal"
            )
            Spacer(Modifier.padding(5.dp))
            CustomTrainingInfoDisplayCard(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                title = "Teplota",
                data = nrfData.teplota,
                unit = "°C"
            )
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

@Composable
fun CustomDailyActivityCard(
    heading: String,
    activity: String,
    activityGoal: String,
    steps: String,
    stepsGoal: String,
    calories: String,
    caloriesGoal: String
) {

    ElevatedCard(
        modifier = Modifier.height(170.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Heading1(heading)
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.width(18.dp),
                        imageVector = Icons.Filled.AccessAlarm,
                        contentDescription = "Icon of activity",
                        tint = Color(0xFF00BCD4)
                    )
                    Spacer(Modifier.padding(horizontal = 2.dp))
                    NormalText(
                        activity,
                        color = if (activity.toFloat() / activityGoal.toFloat() >= 1F) Color(
                            0xFF00BCD4
                        ) else Color.Unspecified
                    )
                    NormalText("/")
                    NormalText(activityGoal)
                    NormalText("min")
                }

                LinearProgressIndicator(
                    progress = { activity.toFloat() / activityGoal.toFloat() },
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF00BCD4)
                )
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.step),
                        contentDescription = "Image of steps",
                        colorFilter = ColorFilter.tint(Color(0xFF4CAF50))
                    )
                    Spacer(Modifier.padding(horizontal = 2.dp))
                    NormalText(
                        steps,
                        color = if (steps.toFloat() / stepsGoal.toFloat() >= 1F) Color(0xFF4CAF50) else Color.Unspecified
                    )
                    NormalText("/")
                    NormalText(stepsGoal)
                }

                LinearProgressIndicator(
                    progress = { steps.toFloat() / stepsGoal.toFloat() },
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF4CAF50)
                )
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.fire),
                        contentDescription = "Image of calories",
                        colorFilter = ColorFilter.tint(Color(0xFFFFC107))
                    )
                    Spacer(Modifier.padding(horizontal = 2.dp))
                    NormalText(
                        calories,
                        color = if (calories.toFloat() / caloriesGoal.toFloat() >= 1F) Color(
                            0xFFFFC107
                        ) else Color.Unspecified
                    )
                    NormalText("/")
                    NormalText(caloriesGoal)
                    NormalText("kcal")
                }

                LinearProgressIndicator(
                    progress = { calories.toFloat() / caloriesGoal.toFloat() },
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFC107)
                )
            }
        }


    }

}

//TODO - este niesom velmi isty na 100% s designom plus by som farby mohol nastavit podla typu
// nameranych dat nejake urcit... (farba ikony a rovnaka ale tmavsia farba karty)
@Composable
fun CustomInfoCardFromDevice(
    heading: String,
    data: String,
    goal: Int = 0,
    unit: String = "",
    image: Int,
    color: Color
) {
    ElevatedCard(
        modifier = Modifier
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.17f)
                .compositeOver(MaterialTheme.colorScheme.surface)
        )
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color.Transparent)
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
                    HeadlineText(
                        if (data.equals("0") || data.equals("0.0")) "--" else data.toString(),
                        fontWeight = FontWeight.Bold
                    )
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
                    Image(
                        modifier = Modifier.fillMaxSize(0.7f),
                        painter = painterResource(image),
                        contentDescription = "Image of card",
                        colorFilter = ColorFilter.tint(color)
                    )
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
    //numberOfParticipants: Int = 0
) {

    Column {

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
                        if (training.isGroupTraining) Heading1("Skupinovy trening") else Heading1("Trening")

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

//                            if (numberOfParticipants > 1) NormalText(
//                                "$numberOfParticipants ucastnici",
//                                color = MaterialTheme.colorScheme.outline
//                            )
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
    data: String = "",
    unit: String = ""
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Heading2(title)
            Spacer(Modifier.padding(3.dp))
            Row {
                Heading1(data, color = MaterialTheme.colorScheme.primary)
                Heading1(unit, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}

@Composable
fun CustomAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit = {},
    dialogTitle: String,
    dialogContent: @Composable () -> Unit,
    icon: @Composable () -> Unit = {},
    isConfirmEnabled: Boolean = true,
    showConfirm: Boolean = true
) {
    AlertDialog(
        icon = {
            icon()
            //Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            dialogContent()
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            if (showConfirm) {
                TextButton(
                    onClick = {
                        onConfirmation()
                    },
                    enabled = isConfirmEnabled
                ) {
                    Text("Confirm")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomAlertDialogGroupTraining(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    groupTraining: GroupTraining? = null,
    trainer: User? = null
) {

    CustomAlertDialog(
        onDismissRequest = { onDismissRequest() },
        onConfirmation = { onConfirmation() },
        dialogTitle = groupTraining?.name?.ifEmpty { trainingList[groupTraining.trainingIndex].name }
            ?: "",
        isConfirmEnabled = groupTraining?.numberOfParticipants != groupTraining?.maxUsers && (groupTraining?.trainingState
            ?: 4) < 4,
        dialogContent = {
            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                Row {
                    if (groupTraining?.name?.isNotEmpty() != false) NormalText(
                        "Typ treningu: " + trainingList[groupTraining?.trainingIndex ?: 0].name
                    )
                }
                Row {
                    NormalText("Datum: ")
                    NormalText(
                        LocalDateTime.ofEpochSecond(
                            groupTraining?.timeDateOfTraining ?: LocalDateTime.now().toEpochSecond(
                                ZoneOffset.UTC
                            ), 0, ZoneOffset.UTC
                        ).format(DateTimeFormatter.ofPattern("d.M.yyyy"))
                    )
                }
                Row {
                    NormalText("Trener: ")
                    NormalText(trainer?.displayName ?: "")
                }
                Row {
                    NormalText("Ucastnici: ")
                    NormalText(
                        (groupTraining?.numberOfParticipants
                            ?: "").toString() + " / " + groupTraining?.maxUsers.toString()
                    )
                }

                if (groupTraining?.trainingDetails?.isNotEmpty() == true) {
                    Spacer(Modifier.padding(3.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        Heading3("Detaily treningu:", fontWeight = FontWeight.SemiBold)
                        NormalText(groupTraining.trainingDetails, textAlign = TextAlign.Start)
                    }
                }
                if ((groupTraining?.trainingState ?: 4) == 4) {
                    Spacer(Modifier.padding(3.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        Heading3("(Trening sa skoncil)", fontWeight = FontWeight.Bold)
                    }
                }

            }

        },
        icon = {
            CustomProfilePictureFrame(
                trainer?.profilePicUrl ?: "",
                frameColor = Color(frameColors[trainer?.color ?: 0])
            )
        }
    )

}

@Composable
fun CustomGroupTrainingParticipantsDetailsCard(
    participant: User,
    training: Training? = null,
    onCardClick: () -> Unit
) {
    val modifier = Modifier.fillMaxWidth()
    val horizontalAlignment = Alignment.CenterHorizontally
    Card(
        modifier = modifier.clickable {
            onCardClick()
        },
        colors = if (participant.color != 0) {
            CardDefaults.cardColors(
                Color(frameColors[participant.color]).copy(
                    alpha = 0.07f
                )
            )
        } else {
            CardDefaults.cardColors()
        }

    ) {

        Column() {

            Row(modifier = modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                CustomProfilePictureFrame(
                    pictureUrl = participant.profilePicUrl,
                    frameColor = Color(frameColors[participant.color]),
                    enabled = false
                )
                Spacer(Modifier.padding(8.dp))
                NormalText(participant.displayName)

            }
            if (training != null) {
                HorizontalDivider()
                Box {
                    Column {
                        Row(
                            modifier = Modifier.height(IntrinsicSize.Min),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    Modifier.padding(8.dp),
                                    horizontalAlignment = horizontalAlignment
                                ) {
                                    NormalText("Priemerna rychlost")
                                    Heading1(training.avgSpeed.toString())
                                }
                            }
                            VerticalDivider(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp)
                            )
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    Modifier.padding(8.dp),
                                    horizontalAlignment = horizontalAlignment
                                ) {
                                    NormalText("Spalene kalorie")
                                    Heading1(training.burnedCalories.toString())
                                }
                            }

                        }
                        HorizontalDivider()
                        Row(
                            modifier = Modifier.height(IntrinsicSize.Min),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    Modifier.padding(8.dp),
                                    horizontalAlignment = horizontalAlignment
                                ) {
                                    NormalText("Priemerny srdcovy tep")
                                    Heading1(training.avgHeartRate.toString())
                                }
                            }
                            VerticalDivider(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp)
                            )
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    Modifier.padding(8.dp),
                                    horizontalAlignment = horizontalAlignment
                                ) {
                                    NormalText("Priemerne tempo")
                                    Heading1(training.avgSpeed.toString())
                                }
                            }

                        }
                        HorizontalDivider()
                        Row(
                            modifier = Modifier.height(IntrinsicSize.Min),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    Modifier.padding(8.dp),
                                    horizontalAlignment = horizontalAlignment
                                ) {
                                    NormalText("Kroky")
                                    Heading1(training.steps.toString())
                                }
                            }
                            VerticalDivider(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp)
                            )
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    Modifier.padding(8.dp),
                                    horizontalAlignment = horizontalAlignment
                                ) {
                                    NormalText("Teplota")
                                    Heading1(training.trainingTemperature.toString())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomModalSheet(
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit,
    stopWatch: StopWatch,
    trainingState: Int,
    receivedUser: User,
    completedTrainingsList: List<Training>,
    followedUsersList: List<User>,
    loggedInUser: User,
    onUnFollowButtonClick: (String) -> Unit,
    onFollowButtonClick: (String) -> Unit,
    loggedInUserFollowedUsersList: List<User>
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomSheetDefaults.DragHandle()
                if (trainingState > 0) {
                    Heading1("Aktualne data")
                } else {
                    Heading1("Profil")
                }
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()
            }
        }
    ) {

        Box(
            modifier = Modifier
                .padding(20.dp),
            contentAlignment = Alignment.TopCenter
        ) {

            if (trainingState > 0) {
                RunningTrainingInfoContent(stopWatch = stopWatch, nrfData = NrfData())
            } else {
                ProfileInfoContent(
                    onUnFollowButtonClick = onUnFollowButtonClick,
                    onFollowButtonClick = onFollowButtonClick,
                    recievedUser = receivedUser,
                    followedUsersList = followedUsersList,
                    completedtrainingsList = completedTrainingsList,
                    loggedInUser = loggedInUser,
                    loggedInUserfollowedUsersList = loggedInUserFollowedUsersList
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewComponents() {
    CustomDailyActivityCard(
        heading = "Denna aktivita",
        activity = "15",
        activityGoal = "45",
        steps = "20",
        stepsGoal = "1000",
        calories = "14",
        caloriesGoal = "400"
    )
}