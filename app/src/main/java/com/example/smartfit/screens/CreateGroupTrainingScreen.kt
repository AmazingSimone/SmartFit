package com.example.smartfit.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.CustomOutlinedTextInput
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.NormalText
import com.example.smartfit.data.GroupTraining
import com.example.smartfit.data.Training
import com.example.smartfit.data.trainingList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupTrainingScreen(
    trainerId: String = "sdv6s4v6s4dv66sdv4",
    onBackClick: () -> Unit,
    onCreateTraining: (GroupTraining) -> Unit
) {

    val nameValue = remember {
        mutableStateOf("")
    }

    val maxUsersValue = remember {
        mutableIntStateOf(0)
    }

    val trainings: List<Training> = trainingList
    var expanded by remember { mutableStateOf(false) }
    var chosenTrainingIndex by remember { mutableIntStateOf(0) }
    val textFieldState = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Heading1("Vytvor skupinovy trening") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back icon"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f)
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
                            maxUsers = maxUsersValue.intValue,
                            trainingIndex = chosenTrainingIndex
                        )
                    )
                },
                enabled = maxUsersValue.intValue > 0 && textFieldState.value.isNotEmpty(),
                buttonText = "Vytvorit trening"
            )
        }

    ) { paddingValues ->

        val (nameOfTraining, maxUsers) = remember { FocusRequester.createRefs() }

        Surface {


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.TopCenter
            ) {

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(Modifier.padding(40.dp))


                    CustomOutlinedTextInput(
                        currentFocusRequester = nameOfTraining,
                        onNext = { nameOfTraining.freeFocus() },
                        keyBoardType = KeyboardType.Text,
                        readOnly = true,
                        label = "Nazov treningu",
                        value = nameValue.value,
                        enterButtonAction = ImeAction.Default,
                        onTextChanged = { nameValue.value = it },
                        supportingText = {
                            NormalText("Nepovinne")
                        }
                    )

                    Spacer(Modifier.padding(8.dp))



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


                    Spacer(Modifier.padding(8.dp))

                    val maxUsersString = remember {
                        mutableStateOf("0")
                    }
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
                        enterButtonAction = ImeAction.Default
                    )
                    Spacer(Modifier.padding(paddingValues))
                }
            }
        }
    }

}

@Preview
@Composable
fun CreateGroupTrainingPreview() {
    CreateGroupTrainingScreen(
        onBackClick = {},
        onCreateTraining = {}
    )
}