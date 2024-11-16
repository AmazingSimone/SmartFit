package com.example.smartfit.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartfit.R
import com.example.smartfit.firebase.signin.SharedFirebaseViewModel
import com.example.smartfit.screens.EditProfileInfoScreen
import com.example.smartfit.screens.LoginScreen
import com.example.smartfit.screens.UserProfileScreen
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavigator(navController: NavHostController = rememberNavController()) {


    GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = stringResource(R.string.default_web_client_id)))

    val firebaseViewModel = viewModel<SharedFirebaseViewModel>()

    NavHost(
        navController = navController,
        startDestination = if (firebaseViewModel.isSignedIn()) Screens.HOME.name else Screens.LOGIN.name
    ) {

        composable(Screens.HOME.name) {

            val sharedUser by firebaseViewModel.sharedUserState.collectAsStateWithLifecycle()
            val isLoading by firebaseViewModel.isLoading.collectAsStateWithLifecycle()

            Log.d("errorFB", "meno ${firebaseViewModel.sharedUserState.value}")


            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    NavigationUpAndBottomBar(

                        recievedUser = sharedUser,
                        onDeviceIndicatorClick = {},
                        onProfilePictureClick = {
                            navController.navigate(Screens.USER_PROFILE.name)
                        },
                        onHistoryClick = {},
                        onQrCodeClick = {},
                        onSearchClick = {}
                    )
                }
            }
        }

        composable(Screens.LOGIN.name) {

            LoginScreen(
                onLoginClick = {
                    firebaseViewModel.checkCurrentUser()
                    navController.navigate(route = Screens.HOME.name)
                }
            )
        }

        composable(Screens.USER_PROFILE.name) {

            val sharedSignedInUser by firebaseViewModel.sharedUserState.collectAsStateWithLifecycle()


            UserProfileScreen(
                recievedUser = sharedSignedInUser,
                onEditClick = { navController.navigate(Screens.EDIT_PROFILE.name) },
                onCloseClick = { navController.popBackStack() },
                onSignOutClick = {
                    firebaseViewModel.signOut()
                    navController.navigate(Screens.LOGIN.name)
                    navController.clearBackStack(Screens.LOGIN.name)
                }
            )
        }

        composable(Screens.EDIT_PROFILE.name) {
            val sharedUser by firebaseViewModel.sharedUserState.collectAsStateWithLifecycle()

            EditProfileInfoScreen(
                recievedUser = sharedUser,
                onBackClick = {
                    navController.popBackStack()
                    navController.clearBackStack(Screens.EDIT_PROFILE.name)
                },
                onSaveClick = {}
            )
        }

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