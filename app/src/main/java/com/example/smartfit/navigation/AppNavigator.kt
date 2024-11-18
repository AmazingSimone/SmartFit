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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartfit.R
import com.example.smartfit.data.trainingList
import com.example.smartfit.firebase.signin.SharedFirebaseViewModel
import com.example.smartfit.screens.CurrentActivityScreen
import com.example.smartfit.screens.EditProfileInfoScreen
import com.example.smartfit.screens.LoginScreen
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
                    onHistoryClick = {},
                    onQrCodeClick = {},
                    onSearchClick = {}
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


            UserProfileScreen(
                recievedUser = sharedSignedInUser,
                loggedInUser = sharedSignedInUser,
                onEditClick = { navController.navigate(Screens.EDIT_PROFILE.name) },
                onCloseClick = { navController.popBackStack() },
                onSignOutClick = {

                    firebaseViewModel.signOut()
                    navController.navigate(Screens.LOGIN.name) {
                        popUpTo(0) { inclusive = true }
                    }
                }
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
                            Toast.makeText(navController.context, "Udaje boli uspesne ulozene",Toast.LENGTH_SHORT).show()
                            firebaseViewModel.checkCurrentUser()
                        } else {
                            Toast.makeText(navController.context, "Nastala chyba pri ukladani udajov",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }

        composable("${Screens.CURRENT_ACTIVITY.name}/{indexOfTraining}") { backStackEntry ->

            val indexOfChosenTraining =
                backStackEntry.arguments?.getString("indexOfTraining")?.toIntOrNull() ?: 0

            CurrentActivityScreen(
                trainingList[indexOfChosenTraining],
                onEndtrainingClick = {

                    if (it.trainingDuration < 5000) {
                        Toast.makeText(
                            navController.context,
                            "Tréning nebol uložený, pretože trval menej ako 5 sekúnd",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                    }

                    navController.navigateUp()
                }
            )

        }

    }


}

fun NavHostController.navigateToSingleTop(route: String) {
    navigate(route) {
        popUpTo(route) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}