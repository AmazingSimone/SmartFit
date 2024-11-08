package com.example.smartfit.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.R
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.Heading2
import com.example.smartfit.components.HeadlineText

@Composable
fun LoginScreen() {

    Surface (
        modifier = Modifier.fillMaxSize()
    ) {

        Box (
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column (
                modifier = Modifier.padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                HeadlineText(stringResource(id = R.string.app_name))
                Spacer(Modifier.padding(10.dp))
                Heading2("Prihlasenie")
                Spacer(Modifier.padding(30.dp))
                CustomButton(
                    //modifier = TODO(),
                    onClick = {},
                    buttonText = "Prihlasenie sa pomocou uctu Google"
                )
            }
        }

    }

}

@Preview
@Composable
fun PreviewLogin() {
    LoginScreen()
}