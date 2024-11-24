package com.example.smartfit.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartfit.R
import com.example.smartfit.data.trainingList
import com.example.smartfit.firebase.signin.SharedFirebaseViewModel
import com.example.smartfit.screens.ActivityDetailScreen
import com.example.smartfit.screens.CreateGroupTrainingScreen
import com.example.smartfit.screens.CurrentActivityScreen
import com.example.smartfit.screens.EditProfileInfoScreen
import com.example.smartfit.screens.LoginScreen
import com.example.smartfit.screens.SearchForUserScreen
import com.example.smartfit.screens.TrainingHistoryScreen
import com.example.smartfit.screens.UserProfileScreen
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigator(navController: NavHostController = rememberNavController()) {


    GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = stringResource(R.string.default_web_client_id)))


    val firebaseViewModel = viewModel<SharedFirebaseViewModel>()
    firebaseViewModel.checkCurrentUser()

    NavHost(
        navController = navController,
        startDestination = if (firebaseViewModel.isSignedIn()) Screens.HOME.name else Screens.LOGIN.name
    ) {

        composable(Screens.HOME.name) {
            val isLoading by firebaseViewModel.isLoading.collectAsStateWithLifecycle()

            val sharedUser by firebaseViewModel.sharedUserState.collectAsStateWithLifecycle()

            val sharedUserFollowingList by firebaseViewModel.sharedUserFollowingState.collectAsStateWithLifecycle()

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center,

                    ) {
                    CircularProgressIndicator()
                }
            } else {
                NavigationUpAndBottomBar(

                    recievedUser = sharedUser,
                    onDeviceIndicatorClick = {},
                    onProfilePictureClick = {
                        firebaseViewModel.viewModelScope.launch {
                            firebaseViewModel.chooseUser(it)

                        }
                        navController.navigate(Screens.USER_PROFILE.name) {
                            popUpTo(Screens.USER_PROFILE.name) {
                                inclusive = true
                                saveState = false
                            }
                        }
                    },
                    onActivityClick = {
                        navController.navigate("${Screens.CURRENT_ACTIVITY.name}/${it}")
                    },
                    onHistoryClick = {
                        navController.navigate(Screens.HISTORY.name)

                    },
                    onQrCodeClick = {},
                    onSearchClick = { navController.navigate(Screens.SEARCH.name) },
                    onUserClick = { userId ->
                        firebaseViewModel.viewModelScope.launch {
                            firebaseViewModel.chooseUser(userId)
                        }
                        navController.navigate(Screens.USER_PROFILE.name)
                    },
                    recievedListOfUsers = sharedUserFollowingList,
                    onFAButtonClick = { trainerId ->
                        navController.navigate("${Screens.CREATE_GROUP_TRAINING.name}/${trainerId}")
                    }
                )
            }
        }

        composable(Screens.LOGIN.name) {

            LoginScreen(
                onLoginClick = {
                    // TODO posli ho na dalsiu obrazovku len ak sa este vytvoria / updatenu data v
                    //  firestore ak nie tak tam daj tlacidlo na zopakovanie
                        recievedFromOneTapButton ->

                    firebaseViewModel.viewModelScope.launch {
                        firebaseViewModel.createUserIfNotExists(recievedFromOneTapButton?.uid!!)
                        navController.navigate(Screens.HOME.name) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screens.USER_PROFILE.name) {

            val sharedSignedInUser by firebaseViewModel.sharedUserState.collectAsStateWithLifecycle()
            val sharedSignedInUserFollowing by firebaseViewModel.sharedUserFollowingState.collectAsStateWithLifecycle()


            val chosenUser = firebaseViewModel.chosenUserState.collectAsStateWithLifecycle()
            val chosenUserCompletedTrainings =
                firebaseViewModel.chosenUserTrainingsState.collectAsStateWithLifecycle()
            val chosenUserFollowing =
                firebaseViewModel.chosenUserFollowingState.collectAsStateWithLifecycle()

            UserProfileScreen(
                recievedUser = chosenUser.value,
                completedtrainingsList = chosenUserCompletedTrainings.value,
                followedUsersList = chosenUserFollowing.value,
                loggedInUser = sharedSignedInUser,
                onEditClick = { navController.navigate(Screens.EDIT_PROFILE.name) },
                onCloseClick = { navController.popBackStack() },
                onSignOutClick = {

                    firebaseViewModel.signOut()
                    navController.navigate(Screens.LOGIN.name) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onUnFollowButtonClick = { userId ->
                    firebaseViewModel.viewModelScope.launch {
                        if (firebaseViewModel.removeUserFromFollowing(userId)) {
                            Toast.makeText(
                                navController.context,
                                "Pouzivatel bol odstraneni zo zoznamu sledovanich",
                                Toast.LENGTH_SHORT
                            ).show()
                            firebaseViewModel.checkCurrentUser()
                        } else {
                            Toast.makeText(
                                navController.context,
                                "Nastala chyba pri ukladani udajov",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                onFollowButtonClick = { userId ->
                    firebaseViewModel.viewModelScope.launch {
                        if (firebaseViewModel.addUserToFollowing(userId)) {
                            Toast.makeText(
                                navController.context,
                                "Sledujes pouzivatela",
                                Toast.LENGTH_SHORT
                            ).show()
                            firebaseViewModel.checkCurrentUser()
                        } else {
                            Toast.makeText(
                                navController.context,
                                "Nastala chyba pri ukladani udajov",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                loggedInUserfollowedUsersList = sharedSignedInUserFollowing
            )
        }

        composable(Screens.EDIT_PROFILE.name) {
            val sharedUser by firebaseViewModel.sharedUserState.collectAsStateWithLifecycle()

            EditProfileInfoScreen(
                recievedUser = sharedUser,
                onBackClick = {

                    navController.navigateUp()
                },
                onSaveClick = {
                    firebaseViewModel.viewModelScope.launch {
                        if (firebaseViewModel.uploadUserData(it)) {
                            Toast.makeText(
                                navController.context,
                                "Udaje boli uspesne ulozene",
                                Toast.LENGTH_SHORT
                            ).show()
                            firebaseViewModel.checkCurrentUser()
                        } else {
                            Toast.makeText(
                                navController.context,
                                "Nastala chyba pri ukladani udajov",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            )
        }

        composable("${Screens.CURRENT_ACTIVITY.name}/{indexOfTraining}") { backStackEntry ->

            val sharedUser by firebaseViewModel.sharedUserState.collectAsStateWithLifecycle()

            //TODO preco tam je getString
            val indexOfChosenTraining =
                backStackEntry.arguments?.getString("indexOfTraining")?.toIntOrNull() ?: 0

            CurrentActivityScreen(
                chosenTraining = trainingList[indexOfChosenTraining],
                onEndtrainingClick = {

                    if (it.trainingDuration < 5000) {
                        Toast.makeText(
                            navController.context,
                            "Tréning nebol uložený, pretože trval menej ako 5 sekúnd",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        firebaseViewModel.viewModelScope.launch {
                            if (firebaseViewModel.uploadTrainingData(indexOfChosenTraining, it)) {
                                Toast.makeText(
                                    navController.context,
                                    "Udaje boli uspesne ulozene",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    navController.context,
                                    "Nastala chyba pri ukladani udajov",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            firebaseViewModel.checkCurrentUser()
                        }
                    }
                    navController.navigateUp()
                }
            )
        }

        composable(Screens.HISTORY.name) {

            val userTrainings by firebaseViewModel.sharedUserTrainingsState.collectAsStateWithLifecycle()

            TrainingHistoryScreen(
                userTrainings,
                onBackClick = {
                    navController.navigateUp()
                },
                onActivityClick = {
                    navController.navigate("${Screens.ACTIVIY_DETAIL.name}/${it}")
                }
            )
        }

        composable("${Screens.ACTIVIY_DETAIL.name}/{indexOfTrainingDetail}") { backStackEntry ->

            val indexOfChosenTraining =
                backStackEntry.arguments?.getString("indexOfTrainingDetail")?.toIntOrNull() ?: 0

            val training by firebaseViewModel.sharedUserTrainingsState.collectAsStateWithLifecycle()

            ActivityDetailScreen(
                training = training[indexOfChosenTraining],
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable(Screens.SEARCH.name) {

            val loggedInUser by firebaseViewModel.sharedUserState.collectAsStateWithLifecycle()
            val searchResults by firebaseViewModel.searchResults.collectAsStateWithLifecycle()
            val followedUsers by firebaseViewModel.sharedUserFollowingState.collectAsStateWithLifecycle()


            SearchForUserScreen(
                loggedInUser = loggedInUser,
                onSearchQuery = { query ->
                    firebaseViewModel.viewModelScope.launch {
                        firebaseViewModel.searchUsers(query)
                    }
                },
                followedUsersList = followedUsers,
                recievedUsersFromQuery = searchResults,
                onUserAddClick = { userId ->
                    firebaseViewModel.viewModelScope.launch {
                        if (firebaseViewModel.addUserToFollowing(userId)) {
                            Toast.makeText(
                                navController.context,
                                "Sledujes pouzivatela",
                                Toast.LENGTH_SHORT
                            ).show()
                            firebaseViewModel.checkCurrentUser()
                        } else {
                            Toast.makeText(
                                navController.context,
                                "Nastala chyba pri ukladani udajov",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                },
                onUserRemoveClick = { userId ->
                    firebaseViewModel.viewModelScope.launch {
                        if (firebaseViewModel.removeUserFromFollowing(userId)) {
                            Toast.makeText(
                                navController.context,
                                "Pouzivatel bol odstraneni zo zoznamu sledovanich",
                                Toast.LENGTH_SHORT
                            ).show()
                            firebaseViewModel.checkCurrentUser()
                        } else {
                            Toast.makeText(
                                navController.context,
                                "Nastala chyba pri ukladani udajov",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                onUserProfileClick = { userId ->
                    firebaseViewModel.viewModelScope.launch {
                        firebaseViewModel.chooseUser(userId)
                    }
                    navController.navigate(Screens.USER_PROFILE.name)
                },
                onBackClick = {

                    navController.navigateUp()
                }
            )
        }

        composable("${Screens.CREATE_GROUP_TRAINING.name}/{trainerId}") { navBackStackEntry ->

            val trainerId = navBackStackEntry.arguments?.getString("trainerId") ?: ""

            CreateGroupTrainingScreen(
                trainerId = trainerId,
                onBackClick = {
                    navController.navigateUp()
                },
                onCreateTraining = { groupTraining ->

                }
            )

        }


    }
}