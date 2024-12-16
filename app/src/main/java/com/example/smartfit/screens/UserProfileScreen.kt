package com.example.smartfit.screens

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.ProfileInfoContent
import com.example.smartfit.data.Training
import com.example.smartfit.data.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun UserProfileScreen(
    onEditClick: () -> Unit,
    onCloseClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onUnFollowButtonClick: (String) -> Unit,
    onFollowButtonClick: (String) -> Unit,
    recievedUser: User,
    followedUsersList: List<User>,
    completedtrainingsList: List<Training>,
    loggedInUser: User?,
    loggedInUserfollowedUsersList: List<User>,

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
        val padding: Dp = 8.dp
        Surface(modifier = Modifier.padding(innerPadding)) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.TopCenter
            ) {

                if (loggedInUser != null) {
                    ProfileInfoContent(
                        onEditClick = onEditClick,
                        onUnFollowButtonClick = onUnFollowButtonClick,
                        onFollowButtonClick = onFollowButtonClick,
                        recievedUser = recievedUser,
                        followedUsersList = followedUsersList,
                        completedtrainingsList = completedtrainingsList,
                        loggedInUser = loggedInUser,
                        loggedInUserfollowedUsersList = loggedInUserfollowedUsersList,
                        enabled = loggedInUser.id == recievedUser.id,
                        editOption = loggedInUser.id == recievedUser.id
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
        recievedUser = User(displayName = "Simon Bartanus", bio = "Hej"),
        loggedInUser = null,
        onUnFollowButtonClick = {},
        onFollowButtonClick = {},
        followedUsersList = emptyList(),
        completedtrainingsList = emptyList(),
        loggedInUserfollowedUsersList = emptyList()
    )
}