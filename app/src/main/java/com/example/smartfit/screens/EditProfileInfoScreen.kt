package com.example.smartfit.screens

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.example.smartfit.components.CustomProfilePictureFrame
import com.example.smartfit.components.CustomSwitch
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.NormalText

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EditProfileInfoScreen() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Heading1("Uprav Profil") },
                actions = {
                    IconButton(onClick = { /* Handle navigation icon press */ }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
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
                onClick = {},
                buttonText = "Ulozit zmeny"
            )
        }

    ) { paddingValues ->

        val (nameFR, surnameFR, birthDateFR, weightFR, heightFR, bioFR) = remember { FocusRequester.createRefs() }

        var selectedColorIndex by remember { mutableStateOf(0) }
        var colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            Color.Blue,
            Color.Red,
            Color.Yellow,
            Color.Magenta,
            Color.Green
        )

        Surface() {


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

                    CustomProfilePictureFrame(
                        frameColor = colors[selectedColorIndex],
                        frameSize = 150.dp
                    )

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {

                        val size: Dp = 50.dp

                        colors.forEachIndexed { index, color ->

                            if (index != 0) {
                                Box(modifier = Modifier
                                    .clickable {
                                        selectedColorIndex = index
                                    }) {
                                    Box(
                                        modifier = Modifier
                                            .size(size)
                                            .background(color),
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
                        label = "Meno",
                        enterButtonAction = ImeAction.Next,
                        onTextChanged = { }
                    )

                    CustomOutlinedTextInput(
                        currentFocusRequester = surnameFR,
                        onNext = { birthDateFR.requestFocus() },
                        keyBoardType = KeyboardType.Text,
                        label = "Priezvisko",
                        enterButtonAction = ImeAction.Next,
                        onTextChanged = { }
                    )

                    CustomDateOutlineInput(
                        currentFocusRequester = birthDateFR,
                        label = "Datum",
                        defaultDate = "",
                        onDateChanged = {}
                    )

                    CustomOutlinedTextInput(
                        currentFocusRequester = weightFR,
                        onNext = { heightFR.requestFocus() },
                        keyBoardType = KeyboardType.Number,
                        label = "Vaha",
                        onTextChanged = {},
                        enterButtonAction = ImeAction.Next,
                        suffix = { NormalText("kg") }
                    )

                    CustomOutlinedTextInput(
                        currentFocusRequester = heightFR,
                        onNext = { bioFR.requestFocus() },
                        keyBoardType = KeyboardType.Number,
                        label = "Vyska",
                        onTextChanged = {},
                        enterButtonAction = ImeAction.Next,
                        suffix = { NormalText("cm") }
                    )

                    CustomOutlinedTextInput(
                        currentFocusRequester = bioFR,
                        onNext = { bioFR.freeFocus() },
                        keyBoardType = KeyboardType.Text,
                        minLines = 5,
                        maxLines = 5,
                        singleLine = false,
                        label = "Bio",
                        onTextChanged = {},
                        enterButtonAction = ImeAction.Done
                    )

                    CustomSwitch(
                        text = "Profil trenera"
                    )
                    Spacer(Modifier.padding(paddingValues))

                }
            }
        }
    }

}

@Preview
@Composable
fun EditProfileInfoPreview() {
    EditProfileInfoScreen()
}