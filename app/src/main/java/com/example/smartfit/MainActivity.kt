package com.example.smartfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.CustomCheckBox
import com.example.smartfit.components.CustomOutlinedTextInput
import com.example.smartfit.components.CustomSwitch
import com.example.smartfit.components.CustomTextButton

import com.example.smartfit.ui.theme.SmartFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            SmartFitTheme {
                Greeting()

            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Greeting() {

    Surface (
        modifier = Modifier.fillMaxSize()
    ) {

        Column {

            //experimental udajne
            val (login, password) = remember { FocusRequester.createRefs() }

            CustomOutlinedTextInput(
                currentFocusRequester = login,
                onNext = { password.requestFocus() },
                label = "Login",
                trailingIcon = Icons.Filled.Person,
                keyBoardType = KeyboardType.Email,
                enterButtonAction = ImeAction.Next,
                isPassword = false,
                errorText = "",
                isError = false
            )

            CustomOutlinedTextInput(
                currentFocusRequester = password,
                onNext = { password.freeFocus() },
                label = "Password",
                trailingIcon = Icons.Filled.Lock,
                keyBoardType = KeyboardType.Password,
                enterButtonAction = ImeAction.Done,
                isPassword = true,
                errorText = "",
                isError = false
            )

            Row {
                CustomButton(
                    enabled = true,
                    onClick = {},
                    buttonText = "Dalej",
                    modifier = Modifier.weight(1f)
                )

                CustomButton(
                    enabled = true,
                    onClick = {},
                    outlined = true,
                    buttonText = "Dalej",
                    modifier = Modifier.weight(1f)
                )

            }

            Row{

                CustomTextButton(
                    onClick = {},
                    buttonText = "Registracia",
                    enabled = true,
                    textColor = Color.Blue,
                    modifier = Modifier.weight(1f)
                    )

                CustomTextButton(
                    onClick = {},
                    buttonText = "Registracia",
                    enabled = true,
                    textColor = Color.Blue,
                    modifier = Modifier.weight(1f)
                    )
            }

            CustomCheckBox(
                text = "Trenersky profil"
            )

            CustomSwitch(
                text = "Profil Trenera"
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Greeting()
}