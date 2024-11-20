package com.example.smartfit.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.CustomProfileInfoTable
import com.example.smartfit.components.CustomProfilePictureFrame
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.Heading2
import com.example.smartfit.components.HeadlineText
import com.example.smartfit.components.NormalText
import com.example.smartfit.data.User
import com.example.smartfit.data.frameColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun UserProfileScreen(
    onEditClick: () -> Unit,
    onCloseClick: () -> Unit,
    onSignOutClick: () -> Unit,
    recievedUser: User,
    loggedInUser: User?
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (recievedUser == loggedInUser) Heading1("Tvoj profil")
                },
                actions = {
                    IconButton(onClick = { onCloseClick() }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                    }
                }
            )
        },
        bottomBar = {
            if (recievedUser == loggedInUser) {
                CustomButton(
                    modifier = Modifier.padding(20.dp),
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    textColor = MaterialTheme.colorScheme.onErrorContainer,
                    onClick = { onSignOutClick() },
                    buttonText = "Odhlasit sa"
                )
            }
        }

    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.TopCenter
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CustomProfilePictureFrame(
                        pictureUrl = recievedUser.profilePicUrl.toString(),
                        editOption = recievedUser == loggedInUser,
                        enabled = recievedUser == loggedInUser,
                        frameColor = Color(frameColors[recievedUser.color]),
                        frameSize = 200.dp,
                        onClick = { onEditClick() }
                    )

                    HeadlineText(recievedUser.displayName ?: "")

                    Heading2("9 Priatelov")

                    NormalText(recievedUser.bio ?: "")

                    CustomProfileInfoTable(
                        avgTimeOfActivity = "0min",
                        avgCountOfSteps = 0,
                        avgBurnedCalories = 0,
                        favouriteTraining = "--"
                    )


                }
            }
        }
    }
}

@Preview
@Composable
fun UserProfilePreview() {
    UserProfileScreen(
        onEditClick = {},
        onCloseClick = {},
        onSignOutClick = {},
        recievedUser = User(),
        loggedInUser = null
    )
}