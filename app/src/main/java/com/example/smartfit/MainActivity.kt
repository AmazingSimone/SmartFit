package com.example.smartfit


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.CustomCheckBox
import com.example.smartfit.components.CustomInfoCardFromDevice
import com.example.smartfit.components.CustomLargeIconButton
import com.example.smartfit.components.CustomOnlineStateIndicator
import com.example.smartfit.components.CustomProfilePictureFrame
import com.example.smartfit.components.CustomSwitch
import com.example.smartfit.components.CustomTextButton
import com.example.smartfit.components.CustomTrainingInfoCardWithDate
import com.example.smartfit.components.CustomTrainingInfoDisplayCard
import com.example.smartfit.navigation.NavigationUpAndBottomBar
import com.example.smartfit.screens.EditProfileInfoScreen
import com.example.smartfit.ui.theme.SmartFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            SmartFitTheme {
                NavigationUpAndBottomBar()

            }
        }
    }
}

@SuppressLint("NewApi")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Greeting() {

    Surface (
        modifier = Modifier.fillMaxSize()
    ) {

        Column (
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
        {

            //experimental udajne
            val (login, password) = remember { FocusRequester.createRefs() }

//            CustomOutlinedTextInput(
//                currentFocusRequester = login,
//                onNext = { password.requestFocus() },
//                label = "Login",
//                trailingIcon = Icons.Filled.Person,
//                keyBoardType = KeyboardType.Email,
//                enterButtonAction = ImeAction.Next,
//                isPassword = false,
//                errorText = "",
//                isError = false,
//                input = TODO(),
//                isDate = TODO()
//            )

//            CustomOutlinedTextInput(
//                currentFocusRequester = password,
//                onNext = { password.freeFocus() },
//                label = "Password",
//                trailingIcon = Icons.Filled.Lock,
//                keyBoardType = KeyboardType.Password,
//                enterButtonAction = ImeAction.Done,
//                isPassword = true,
//                errorText = "",
//                isError = false,
//                input = TODO(),
//                isDate = TODO()
//            )

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

            CustomLargeIconButton(
                onClick = {},
                icon = Icons.Filled.Add
            )

            CustomOnlineStateIndicator(
                onClick = {}
            )

            CustomProfilePictureFrame()

            CustomInfoCardFromDevice(
                heading = "Kalorie",
                data = 15,
                goal = 105,

                unit = "kcal",
                icon = Icons.Filled.MonitorHeart
            )

            Spacer(modifier = Modifier.height(20.dp))


            CustomTrainingInfoCardWithDate(
                trainingType = "Beh",
                numberOfParticipants = 3,
                trainingIcon = Icons.AutoMirrored.Filled.DirectionsRun
            )

            CustomTrainingInfoDisplayCard(
                title = "Vzdialenost",
                data = 1.1f,
                unit = "km"
            )


            
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Greeting()
}