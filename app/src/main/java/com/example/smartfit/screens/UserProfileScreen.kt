package com.example.smartfit.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.ProfileInfoContent
import com.example.smartfit.data.Training
import com.example.smartfit.data.User
import com.example.smartfit.data.frameColors

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun UserProfileScreen(
    onEditClick: () -> Unit,
    onBackClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onUnFollowButtonClick: (String) -> Unit,
    onFollowButtonClick: (String) -> Unit,
    recievedUser: User,
    followedUsersList: List<User>,
    completedtrainingsList: List<Training>,
    loggedInUser: User?,
    loggedInUserfollowedUsersList: List<User>,

    ) {

    val isLoading = rememberSaveable { mutableStateOf(true) }

    BackHandler {
        onBackClick()
    }

    LaunchedEffect(
        recievedUser,
        followedUsersList,
        completedtrainingsList,
        loggedInUser,
        loggedInUserfollowedUsersList
    ) {
        isLoading.value = recievedUser == User() || loggedInUser == User()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (recievedUser == loggedInUser) Heading1("Tvoj profil")
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back icon"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (recievedUser.color != 0)
                        Color(frameColors[recievedUser.color]).copy(
                            alpha = 0.07f
                        )
                    else
                        MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
                BottomAppBar(
                    containerColor = if (recievedUser.color != 0)
                        Color(frameColors[recievedUser.color]).copy(
                            alpha = 0.07f
                        )
                    else
                        MaterialTheme.colorScheme.background
                ) {
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
        },

    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {

            if (isLoading.value) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (recievedUser.color != 0)
                                Color(frameColors[recievedUser.color]).copy(
                                    alpha = 0.07f
                                )
                            else
                                MaterialTheme.colorScheme.background
                        ),
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
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun UserProfilePreview() {
    UserProfileScreen(
        onEditClick = {},
        onBackClick = {},
        onSignOutClick = {},
        recievedUser = User(displayName = "Simon Bartanus", bio = "Hej"),
        loggedInUser = User(displayName = "Simon Bartanus", bio = "Hej"),
        onUnFollowButtonClick = {},
        onFollowButtonClick = {},
        followedUsersList = emptyList(),
        completedtrainingsList = emptyList(),
        loggedInUserfollowedUsersList = emptyList()
    )
}