package com.example.smartfit.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomAlertDialog
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.CustomOutlinedTextInput
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.NormalText
import com.example.smartfit.data.GroupTraining
import com.example.smartfit.data.Training
import com.example.smartfit.data.trainingList
import java.time.LocalDateTime
import java.time.ZoneOffset


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupTrainingScreen(
    trainerId: String = "",
    onBackClick: () -> Unit,
    onCreateTraining: (GroupTraining) -> Unit
) {

    val nameValue = rememberSaveable {
        mutableStateOf("")
    }

    val maxUsersValue = rememberSaveable {
        mutableIntStateOf(0)
    }
    val maxUsersString = rememberSaveable {
        mutableStateOf("0")
    }

    val detailsValue = rememberSaveable {
        mutableStateOf("")
    }

    val trainings: List<Training> = trainingList
    var expanded by rememberSaveable { mutableStateOf(false) }
    var chosenTrainingIndex by rememberSaveable { mutableIntStateOf(0) }
    val textFieldState = rememberSaveable { mutableStateOf("") }

    val showAlertDialog = rememberSaveable { mutableStateOf(false) }

    val validator =
        { nameValue.value.isNotEmpty() || maxUsersValue.intValue > 0 || detailsValue.value.isNotEmpty() || textFieldState.value != "" }

    BackHandler {
        if (validator()) {
            showAlertDialog.value = true
        } else {
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Heading1("Vytvor skupinovy trening") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (validator()) {
                            showAlertDialog.value = true
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
        },
        bottomBar = {
            CustomButton(
                modifier = Modifier.padding(20.dp),
                onClick = {

                    onCreateTraining(
                        GroupTraining(
                            trainerId = trainerId,
                            name = nameValue.value,
                            trainingIndex = chosenTrainingIndex,
                            timeDateOfTraining = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                            maxUsers = maxUsersValue.intValue,
                            trainingDetails = detailsValue.value
                        )
                    )
                },
                enabled = maxUsersValue.intValue > 0 && textFieldState.value.isNotEmpty(),
                buttonText = "Vytvorit trening"
            )
        }

    ) { paddingValues ->

        val (nameOfTraining, maxUsers, detailsOfTraining) = remember { FocusRequester.createRefs() }

        Surface(modifier = Modifier.padding(paddingValues)) {

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.padding(6.dp))


                CustomOutlinedTextInput(
                    currentFocusRequester = nameOfTraining,
                    onNext = { nameOfTraining.freeFocus() },
                    keyBoardType = KeyboardType.Text,
                    label = "Nazov treningu",
                    value = nameValue.value,
                    enterButtonAction = ImeAction.Default,
                    onTextChanged = { nameValue.value = it },
                    supportingText = {
                        NormalText("Nepovinne")
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Training name icon"
                        )
                    }
                )

                Spacer(Modifier.padding(6.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                ) {

                    CustomOutlinedTextInput(
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                        value = textFieldState.value,
                        currentFocusRequester = nameOfTraining,
                        label = "Zvol typ treningu",
                        onTextChanged = { textFieldState.value = it },
                        readOnly = true,
                        singleLine = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        trainings.forEach { training ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        training.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                onClick = {
                                    textFieldState.value = training.name
                                    chosenTrainingIndex = trainings.indexOf(training)
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }


                Spacer(Modifier.padding(12.dp))


                CustomOutlinedTextInput(
                    currentFocusRequester = maxUsers,
                    onNext = { nameOfTraining.freeFocus() },
                    keyBoardType = KeyboardType.Number,
                    label = "Pocet ucastnikov",
                    value = maxUsersString.value,
                    onTextChanged = {
                        maxUsersString.value = it
                        maxUsersValue.intValue = it.toIntOrNull() ?: 0
                    },
                    enterButtonAction = ImeAction.Default,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.People,
                            contentDescription = "People count icon"
                        )
                    }
                )

                Spacer(Modifier.padding(12.dp))

                CustomOutlinedTextInput(
                    currentFocusRequester = detailsOfTraining,
                    onNext = { detailsOfTraining.freeFocus() },
                    keyBoardType = KeyboardType.Text,
                    label = "Detaily treningu",
                    value = detailsValue.value,
                    enterButtonAction = ImeAction.Default,
                    onTextChanged = { detailsValue.value = it },
                    supportingText = {
                        NormalText("Nepovinne")
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.EditNote,
                            contentDescription = "Training details icon"
                        )
                    },
                    singleLine = false,
                    maxLines = 6,
                    minLines = 6
                )
                Spacer(Modifier.padding(paddingValues))
            }
        }
    }

    if (showAlertDialog.value) {
        CustomAlertDialog(
            onDismissRequest = { showAlertDialog.value = false },
            onConfirmation = {
                showAlertDialog.value = false
                onBackClick()
            },
            dialogTitle = "Upozornenie",
            dialogContent = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    NormalText("Treningove udaje budu stratene")
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Warning icon"
                )
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CreateGroupTrainingPreview() {
    CreateGroupTrainingScreen(
        onBackClick = {},
        onCreateTraining = {}
    )
}