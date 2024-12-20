package com.example.smartfit.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.CustomDateOutlineInput
import com.example.smartfit.components.CustomOutlinedTextInput
import com.example.smartfit.components.CustomSwitch
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.Heading2
import com.example.smartfit.components.NormalText
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

    //var currentUser: User? = recievedUser
    var currentUser by remember { mutableStateOf(recievedUser) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Heading1("Uprav Profil") },
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
                    onSaveClick(currentUser)
                    Log.d("AHOJ", "save $currentUser")
                },
                enabled = (currentUser != recievedUser) && (currentUser.activityGoal.toInt() != 0 && currentUser.stepsGoal.toInt() != 0 && currentUser.caloriesGoal.toInt() != 0),
                buttonText = "Ulozit zmeny"
            )
        }

    ) { paddingValues ->

        val (nameFR, surnameFR, birthDateFR, weightFR, heightFR, activityGoal, stepsGoal, caloriesGoal, bioFR) = remember { FocusRequester.createRefs() }

        var selectedColorIndex by remember { mutableIntStateOf(currentUser.color) }
        val colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            Color.Blue,
            Color.Red,
            Color.Yellow,
            Color.Magenta,
            Color.Green
        )

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

//                    CustomProfilePictureFrame(
//                        frameColor = colors[selectedColorIndex],
//                        frameSize = 150.dp
//                    )

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

                        //colors
                        frameColors.forEachIndexed { index, color ->

                            if (index != 0) {
                                Box(modifier = Modifier
                                    .clickable {
                                        selectedColorIndex = index
                                        //selectedColor = color
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
                        value = recievedUser.displayName.toString(),
                        enterButtonAction = ImeAction.Next,
                        onTextChanged = { }
                    )

//                    CustomOutlinedTextInput(
//                        currentFocusRequester = surnameFR,
//                        onNext = { birthDateFR.requestFocus() },
//                        keyBoardType = KeyboardType.Text,
//                        label = "Priezvisko",
//                        enterButtonAction = ImeAction.Next,
//                        onTextChanged = { }
//                    )
                    Spacer(Modifier.padding(8.dp))

                    CustomDateOutlineInput(
                        currentFocusRequester = birthDateFR,
                        label = "Datum",
                        defaultDate = currentUser.birthDate,
                        //if (currentUser?.birthDate == 0L) LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC) * 1000 else currentUser?.birthDate ?: LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC),

                        onDateChanged = {
                            currentUser = currentUser.copy(birthDate = it ?: 0L)
                            //currentUser = currentUser?.copy(birthDate = it)
                        }
                    )
                    Spacer(Modifier.padding(8.dp))

                    //TODO na toto bude pravdepodobne treba viewmodel
                    val weightValue = remember {
                        mutableStateOf(currentUser.weight.toString())
                    }
                    CustomOutlinedTextInput(
                        currentFocusRequester = weightFR,
                        onNext = { heightFR.requestFocus() },
                        keyBoardType = KeyboardType.Number,
                        label = "Vaha",
                        value = if (weightValue.value == "0.0") "0" else weightValue.value,
                        onTextChanged = {
                            weightValue.value = it.replace(",", ".")
                            currentUser = currentUser.copy(weight = it.toFloatOrNull() ?: 0F)
                        },
                        enterButtonAction = ImeAction.Next,
                        suffix = { NormalText("kg") }
                    )
                    Spacer(Modifier.padding(8.dp))
                    val heightValue = remember {
                        mutableStateOf(currentUser.height.toString())
                    }
                    CustomOutlinedTextInput(
                        currentFocusRequester = heightFR,
                        onNext = { activityGoal.requestFocus() },
                        keyBoardType = KeyboardType.Number,
                        label = "Vyska",
                        value = if (heightValue.value == "0.0") "0" else heightValue.value,
                        onTextChanged = {
                            heightValue.value = it.replace(",", ".")
                            currentUser = currentUser.copy(height = it.toFloatOrNull() ?: 0F)
                        },
                        enterButtonAction = ImeAction.Next,
                        suffix = { NormalText("cm") }
                    )
                    Spacer(Modifier.padding(8.dp))
                    val activityGoalValue = remember {
                        mutableStateOf(currentUser.activityGoal)
                    }
                    CustomOutlinedTextInput(
                        currentFocusRequester = activityGoal,
                        onNext = { stepsGoal.requestFocus() },
                        keyBoardType = KeyboardType.Number,
                        label = "Cielova doba aktivity za den",
                        value = activityGoalValue.value,
                        onTextChanged = {
                            activityGoalValue.value = it.filter { char -> char.isDigit() }
                            currentUser =
                                currentUser.copy(activityGoal = if (activityGoalValue.value.isEmpty()) "0" else activityGoalValue.value)
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
                    Spacer(Modifier.padding(3.dp))
                    val stepsGoalValue = remember {
                        mutableStateOf(currentUser.stepsGoal)
                    }
                    CustomOutlinedTextInput(
                        currentFocusRequester = stepsGoal,
                        onNext = { caloriesGoal.requestFocus() },
                        keyBoardType = KeyboardType.Number,
                        label = "Cielovy pocet krokov za den",
                        value = stepsGoalValue.value,
                        onTextChanged = {
                            stepsGoalValue.value = it.filter { char -> char.isDigit() }
                            currentUser =
                                currentUser.copy(stepsGoal = if (stepsGoalValue.value.isEmpty()) "0" else stepsGoalValue.value)
                        },
                        enterButtonAction = ImeAction.Next,
                        isError = (stepsGoalValue.value.toIntOrNull() ?: 0) <= 0,
                        supportingText = {
                            if ((stepsGoalValue.value.toIntOrNull()
                                    ?: 0) <= 0
                            ) NormalText("Hodnota musi byt vacsia ako 0")
                        }
                    )
                    Spacer(Modifier.padding(3.dp))
                    val caloriesGoalValue = remember {
                        mutableStateOf(currentUser.caloriesGoal)
                    }
                    CustomOutlinedTextInput(
                        currentFocusRequester = caloriesGoal,
                        onNext = { bioFR.requestFocus() },
                        keyBoardType = KeyboardType.Number,
                        label = "Cielovy pocet spal. kalorii za den",
                        value = caloriesGoalValue.value,
                        onTextChanged = {
                            caloriesGoalValue.value = it.filter { char -> char.isDigit() }
                            currentUser =
                                currentUser.copy(caloriesGoal = if (caloriesGoalValue.value.isEmpty()) "0" else caloriesGoalValue.value)
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
                    Spacer(Modifier.padding(3.dp))
                    CustomOutlinedTextInput(
                        currentFocusRequester = bioFR,
                        onNext = { bioFR.freeFocus() },
                        keyBoardType = KeyboardType.Text,
                        minLines = 5,
                        maxLines = 5,
                        singleLine = false,
                        label = "Bio",
                        value = currentUser.bio,
                        onTextChanged = {
                            currentUser = currentUser.copy(bio = it)
                        },
                        enterButtonAction = ImeAction.Done
                    )
                    Spacer(Modifier.padding(8.dp))
                    CustomSwitch(
                        text = "Profil trenera",
                        defaultPosition = remember { mutableStateOf(currentUser.isTrainer) }.value,
                        onSwitchChange = {
                            currentUser = currentUser.copy(isTrainer = it)
                        }
                    )
                    Spacer(Modifier.padding(paddingValues))
                }
            }
        }
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