package com.example.smartfit.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.R
import com.example.smartfit.components.CustomOneTapUiButtonWithFirebaseAuth
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.HeadlineText
import dev.gitlive.firebase.auth.FirebaseUser

@Composable
fun LoginScreen(
    onLoginClick: (FirebaseUser?) -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                HeadlineText(stringResource(id = R.string.app_name))

                Spacer(Modifier.padding(10.dp))

                Heading1("Prihlasenie")

                Spacer(Modifier.padding(30.dp))


                CustomOneTapUiButtonWithFirebaseAuth(
                    modifier = Modifier.fillMaxWidth(),
                    onFirebaseResult = { result ->
                        if (result.isSuccess) {
                            onLoginClick(result.getOrNull())
                        } else {
                            Log.d("errorFB", "error")
                        }
                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewLogin() {
    LoginScreen(
        onLoginClick = {}
    )
}