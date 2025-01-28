package com.example.smartfit.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomAlertDialog
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.CustomOutlinedTextInput
import com.example.smartfit.components.CustomSwitch
import com.example.smartfit.components.DatePickerModal
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.Heading2
import com.example.smartfit.components.NormalText
import com.example.smartfit.components.convertMillisToDate
import com.example.smartfit.data.User
import com.example.smartfit.data.frameColors

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditProfileInfoScreen(
    onBackClick: () -> Unit,
    onSaveClick: (User) -> Unit,
    recievedUser: User
) {

    var currentUser by remember { mutableStateOf(recievedUser) }

    var selectedColorIndex by rememberSaveable { mutableIntStateOf(currentUser.color) }

    var selectedDate by rememberSaveable { mutableStateOf<Long?>(currentUser.birthDate) }
    var showModal by rememberSaveable { mutableStateOf(false) }

    var showAlertDialog by rememberSaveable { mutableStateOf(false) }

    val weightValue = rememberSaveable {
        mutableStateOf(currentUser.weight.toString())
    }

    val heightValue = rememberSaveable {
        mutableStateOf(currentUser.height.toString())
    }

    val activityGoalValue = rememberSaveable {
        mutableStateOf(currentUser.activityGoal)
    }

    val stepsGoalValue = rememberSaveable {
        mutableStateOf(currentUser.stepsGoal)
    }

    val caloriesGoalValue = rememberSaveable {
        mutableStateOf(currentUser.caloriesGoal)
    }

    val bioValue = rememberSaveable {
        mutableStateOf(currentUser.bio)
    }

    val isTrainer = rememberSaveable { mutableStateOf(currentUser.isTrainer) }

    val validator = {
        recievedUser.let {

            it.color != selectedColorIndex ||
                    it.birthDate != selectedDate ||
                    it.weight != weightValue.value.toFloatOrNull() ||
                    it.height != heightValue.value.toFloatOrNull() ||
                    it.activityGoal != activityGoalValue.value ||
                    it.stepsGoal != stepsGoalValue.value ||
                    it.caloriesGoal != caloriesGoalValue.value ||
                    it.bio != bioValue.value ||
                    it.isTrainer != isTrainer.value
        } && (
                selectedDate != null &&
                        weightValue.value != "" && weightValue.value != "0" &&
                        heightValue.value != "" && heightValue.value != "0" &&
                        activityGoalValue.value != "" && activityGoalValue.value != "0" &&
                        stepsGoalValue.value != "" && stepsGoalValue.value != "0" &&
                        caloriesGoalValue.value != "" && caloriesGoalValue.value != "0")
    }

    BackHandler {
        if (validator()) {
            showAlertDialog = true
        } else {
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Heading1("Uprav Profil") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (validator()) {
                            showAlertDialog = true
                        } else {
                            onBackClick()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back icon"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }

    ) { paddingValues ->

        val (nameFR, surnameFR, birthDateFR, weightFR, heightFR, activityGoal, stepsGoal, caloriesGoal, bioFR) = remember { FocusRequester.createRefs() }

        Surface(modifier = Modifier.padding(paddingValues)) {

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.padding(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Heading2("Farba ramika:")
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    val size: Dp = 50.dp

                    frameColors.forEachIndexed { index, color ->

                        if (index != 0) {
                            Box(modifier = Modifier
                                .clickable {
                                    selectedColorIndex = index
                                    currentUser = currentUser.copy(color = selectedColorIndex)
                                }) {
                                Box(
                                    modifier = Modifier
                                        .size(size)
                                        .background(Color(color)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (selectedColorIndex == index) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected color icon",
                                            tint = MaterialTheme.colorScheme.surface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                CustomOutlinedTextInput(
                    currentFocusRequester = nameFR,
                    onNext = { surnameFR.requestFocus() },
                    keyBoardType = KeyboardType.Text,
                    readOnly = true,
                    label = "Meno",
                    value = recievedUser.displayName,
                    enterButtonAction = ImeAction.Next,
                    onTextChanged = { }
                )

                Spacer(Modifier.padding(12.dp))

                CustomOutlinedTextInput(
                    currentFocusRequester = birthDateFR,
                    value = (if (selectedDate != 0L) convertMillisToDate(
                        selectedDate ?: currentUser.birthDate
                    ) else ""),
                    label = "Datum",
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
                        },
                        onDismiss = { showModal = false }
                    )
                }

                Spacer(Modifier.padding(12.dp))

                CustomOutlinedTextInput(
                    currentFocusRequester = weightFR,
                    onNext = { heightFR.requestFocus() },
                    keyBoardType = KeyboardType.Number,
                    label = "Vaha",
                    value = if (weightValue.value == "0.0") "0" else weightValue.value,
                    onTextChanged = {
                        weightValue.value = it.replace(",", ".")
                    },
                    enterButtonAction = ImeAction.Next,
                    suffix = { NormalText("kg") }
                )
                Spacer(Modifier.padding(12.dp))

                CustomOutlinedTextInput(
                    currentFocusRequester = heightFR,
                    onNext = { activityGoal.requestFocus() },
                    keyBoardType = KeyboardType.Number,
                    label = "Vyska",
                    value = if (heightValue.value == "0.0") "0" else heightValue.value,
                    onTextChanged = {
                        heightValue.value = it.replace(",", ".")
                    },
                    enterButtonAction = ImeAction.Next,
                    suffix = { NormalText("cm") }
                )
                Spacer(Modifier.padding(12.dp))

                CustomOutlinedTextInput(
                    currentFocusRequester = activityGoal,
                    onNext = { stepsGoal.requestFocus() },
                    keyBoardType = KeyboardType.Number,
                    label = "Cielova doba aktivity za den",
                    value = activityGoalValue.value,
                    onTextChanged = {
                        activityGoalValue.value = it.filter { char -> char.isDigit() }
                    },
                    enterButtonAction = ImeAction.Next,
                    suffix = { NormalText("min.") },
                    isError = (activityGoalValue.value.toIntOrNull() ?: 0) <= 0,
                    supportingText = {
                        if ((activityGoalValue.value.toIntOrNull()
                                ?: 0) <= 0
                        ) NormalText("Hodnota musi byt vacsia ako 0")
                    }
                )
                Spacer(Modifier.padding(6.dp))

                CustomOutlinedTextInput(
                    currentFocusRequester = stepsGoal,
                    onNext = { caloriesGoal.requestFocus() },
                    keyBoardType = KeyboardType.Number,
                    label = "Cielovy pocet krokov za den",
                    value = stepsGoalValue.value,
                    onTextChanged = {
                        stepsGoalValue.value = it.filter { char -> char.isDigit() }
                    },
                    enterButtonAction = ImeAction.Next,
                    isError = (stepsGoalValue.value.toIntOrNull() ?: 0) <= 0,
                    supportingText = {
                        if ((stepsGoalValue.value.toIntOrNull()
                                ?: 0) <= 0
                        ) NormalText("Hodnota musi byt vacsia ako 0")
                    }
                )
                Spacer(Modifier.padding(6.dp))

                CustomOutlinedTextInput(
                    currentFocusRequester = caloriesGoal,
                    onNext = { bioFR.requestFocus() },
                    keyBoardType = KeyboardType.Number,
                    label = "Cielovy pocet spal. kalorii za den",
                    value = caloriesGoalValue.value,
                    onTextChanged = {
                        caloriesGoalValue.value = it.filter { char -> char.isDigit() }
                    },
                    enterButtonAction = ImeAction.Next,
                    suffix = { NormalText("kcal") },
                    isError = (caloriesGoalValue.value.toIntOrNull() ?: 0) <= 0,
                    supportingText = {
                        if ((caloriesGoalValue.value.toIntOrNull()
                                ?: 0) <= 0
                        ) NormalText("Hodnota musi byt vacsia ako 0")
                    }
                )
                Spacer(Modifier.padding(6.dp))
                CustomOutlinedTextInput(
                    currentFocusRequester = bioFR,
                    onNext = { bioFR.freeFocus() },
                    keyBoardType = KeyboardType.Text,
                    minLines = 5,
                    maxLines = 5,
                    singleLine = false,
                    label = "Bio",
                    value = bioValue.value,
                    onTextChanged = {
                        bioValue.value = it
                    },
                    enterButtonAction = ImeAction.Done
                )
                Spacer(Modifier.padding(12.dp))
                CustomSwitch(
                    text = "Profil trenera",
                    defaultPosition = isTrainer.value,
                    onSwitchChange = {
                        isTrainer.value = it
                    }
                )
                Spacer(Modifier.padding(12.dp))

                CustomButton(
                    onClick = {
                        onSaveClick(
                            currentUser.copy(
                                color = selectedColorIndex,
                                birthDate = selectedDate ?: 0L,
                                weight = weightValue.value.toFloatOrNull() ?: 0F,
                                height = heightValue.value.toFloatOrNull() ?: 0F,
                                activityGoal = activityGoalValue.value,
                                stepsGoal = stepsGoalValue.value,
                                caloriesGoal = caloriesGoalValue.value,
                                bio = bioValue.value,
                                isTrainer = isTrainer.value
                            )
                        )
                    },
                    enabled = validator(),
                    buttonText = "Ulozit zmeny"
                )
                Spacer(Modifier.padding(12.dp))
            }
        }
    }
    if (showAlertDialog) {
        CustomAlertDialog(
            onDismissRequest = { showAlertDialog = false },
            onConfirmation = {
                showAlertDialog = false
                onBackClick()
            },
            dialogTitle = "Upozornenie",
            dialogContent = { NormalText("Naozaj chcete opustit stranku bez ulozenia zmien?") },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Loose of changes waring icon"
                )
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun EditProfileInfoPreview() {
    EditProfileInfoScreen(
        onBackClick = {},
        onSaveClick = {},
        recievedUser = User()
    )
}