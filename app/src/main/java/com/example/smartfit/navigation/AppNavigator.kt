package com.example.smartfit.navigation

import QrReaderScreen
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.smartfit.ble_device.BLEClient
import com.example.smartfit.data.GroupTraining
import com.example.smartfit.data.Training
import com.example.smartfit.data.User
import com.example.smartfit.data.trainingList
import com.example.smartfit.firebase.signin.SharedFirebaseViewModel
import com.example.smartfit.screens.ActivityDetailScreen
import com.example.smartfit.screens.CreateGroupTrainingScreen
import com.example.smartfit.screens.CurrentActivityScreen
import com.example.smartfit.screens.DeviceScreen
import com.example.smartfit.screens.EditProfileInfoScreen
import com.example.smartfit.screens.GroupTrainingLobby
import com.example.smartfit.screens.LoginScreen
import com.example.smartfit.screens.SearchForUserScreen
import com.example.smartfit.screens.TrainingHistoryScreen
import com.example.smartfit.screens.UserProfileScreen
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigator(navController: NavHostController = rememberNavController(), bleClient: BLEClient) {

    GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = stringResource(R.string.default_web_client_id)))

    val firebaseViewModel = viewModel<SharedFirebaseViewModel>()
    firebaseViewModel.getCurrentUserDataFromFirebase()

    val bleConnectionState by bleClient.stateOfDevice.collectAsStateWithLifecycle()

    LaunchedEffect(bleConnectionState) {
        when (bleConnectionState) {
            2 -> {
                Toast.makeText(
                    navController.context,
                    "Zariadenie je pripravene",
                    Toast.LENGTH_SHORT
                ).show()
            }

            1 -> {
                Toast.makeText(
                    navController.context,
                    "Zariadenie sa pripaja",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
//                Toast.makeText(
//                    navController.context,
//                    "Pripojte zariadenie",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (firebaseViewModel.isSignedIn()) Screens.HOME.name else Screens.LOGIN.name
    ) {

        composable(Screens.HOME.name) {
            val isLoading by firebaseViewModel.isLoading.collectAsStateWithLifecycle()

            val sharedUser by firebaseViewModel.sharedUserState.collectAsStateWithLifecycle()

            val sharedUserFollowingList by firebaseViewModel.sharedUserFollowingState.collectAsStateWithLifecycle()

            val listOfTrainings by firebaseViewModel.sharedUserTrainingsState.collectAsStateWithLifecycle()

            //val bleConnectionState by bleClient.stateOfDevice.collectAsStateWithLifecycle()

            val bleData by bleClient.data.collectAsStateWithLifecycle()
            Log.d("AHOJBLE", "$bleData")

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
                    onDeviceIndicatorClick = {
                        navController.navigate(Screens.DEVICE_SCREEN.name)
                    },
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
                        when (bleConnectionState) {
                            2 -> {
                                bleClient.resetCharacteristic()
                                navController.navigate("${Screens.CURRENT_ACTIVITY.name}/${it}")
                            }

                            1 -> {
                                Toast.makeText(
                                    navController.context,
                                    "Pockaj kym bude zariadenie pripravene",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            else -> {
                                Toast.makeText(
                                    navController.context,
                                    "Najskor sa pripoj k zariadeniu",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    onHistoryClick = {
                        navController.navigate(Screens.HISTORY.name)

                    },
                    onQrCodeClick = {
                        navController.navigate(Screens.QR_READER_SCREEN.name)
                    },
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
                    },
                    isBLEConnected = bleConnectionState,
                    nrfData = bleData,
                    listOfTrainings = listOfTrainings
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
                        firebaseViewModel.createUserIfNotExists(recievedFromOneTapButton?.uid ?: "")
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
                onBackClick = { navController.popBackStack() },
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
                                "Pouzivatel bol odstraneny zo zoznamu sledovanich",
                                Toast.LENGTH_SHORT
                            ).show()
                            firebaseViewModel.getCurrentUserDataFromFirebase()
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
                            firebaseViewModel.getCurrentUserDataFromFirebase()
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
                    //TODO toto inac vobec nefunguje stale mozem rychlim dvojklikom popnut ostatne obrazovky
                    firebaseViewModel.viewModelScope.launch {
                        firebaseViewModel.chooseUser(sharedUser.id)
                    }
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
                            firebaseViewModel.getCurrentUserDataFromFirebase()
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

            val participantOfTraining by firebaseViewModel.sharedUserState.collectAsStateWithLifecycle() 
            val chosenGroupTraining by firebaseViewModel.chosenGroupTrainingState.collectAsStateWithLifecycle()
            val chosenTraining by firebaseViewModel.chosenTrainingState.collectAsStateWithLifecycle()
            val bleData by bleClient.data.collectAsStateWithLifecycle()

            //TODO preco tam je getString
            val indexOfChosenTraining =
                backStackEntry.arguments?.getString("indexOfTraining")?.toIntOrNull() ?: 0

            CurrentActivityScreen(
                createdTraining = chosenTraining,
                indexOfTraining = indexOfChosenTraining,
                onEndTraining = {

                    if (it.trainingDuration < 5000) {
                        Toast.makeText(
                            navController.context,
                            "Tréning nebol uložený, pretože trval menej ako 5 sekúnd",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        firebaseViewModel.viewModelScope.launch {
                            if (firebaseViewModel.uploadLoggedInUserTrainingData(
                                    indexOfChosenTraining,
                                    it
                                )
                            ) {
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
                            firebaseViewModel.getCurrentUserDataFromFirebase()
                        }
                    }
                    navController.navigate(Screens.HOME.name) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                chosenGroupTraining = chosenGroupTraining,
                onCheckAllTrainingInfo = {

                    runBlocking {
                        firebaseViewModel.fetchGroupTrainingData(chosenGroupTraining.id)
                        firebaseViewModel.fetchAllParticipantsOfTraining(chosenGroupTraining.id)
                    }
                },
                nrfData = bleData,
                participant = participantOfTraining,
                onCreateTraining = { groupTrainingId ->
                    firebaseViewModel.viewModelScope.launch {
                        firebaseViewModel.createLoggedInUserTraining(
                            indexOfChosenTraining,
                            groupTrainingId
                        )
                    }
                },
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

            val firebaseSharedUser by firebaseViewModel.sharedUserState.collectAsStateWithLifecycle()

            val chosenUser = firebaseViewModel.chosenUserState.collectAsStateWithLifecycle()
            val chosenUserCompletedTrainings =
                firebaseViewModel.chosenUserTrainingsState.collectAsStateWithLifecycle()
            val chosenUserFollowing =
                firebaseViewModel.chosenUserFollowingState.collectAsStateWithLifecycle()
            val sharedSignedInUserFollowing by firebaseViewModel.sharedUserFollowingState.collectAsStateWithLifecycle()

            ActivityDetailScreen(
                training = training[indexOfChosenTraining],
                trainerDetails = {
                    var trainer: User?
//                    firebaseViewModel.viewModelScope.launch {
//                        trainer = firebaseViewModel.getUserData(firebaseViewModel.getGroupTrainingData(training[indexOfChosenTraining].id)?.trainerId ?: "")
//                    }
                    runBlocking {
                        trainer = firebaseViewModel.getUserData(
                            firebaseViewModel.getGroupTrainingData(training[indexOfChosenTraining].id)?.trainerId
                                ?: ""
                        )
                    }
                    trainer
                },
                listOfParticipantsIdsOfGroupTraining = { trainingId ->
                    var participants: List<String>
                    runBlocking {
                        participants = firebaseViewModel.getParticipantsOfGroupTraining(trainingId)
                    }
                    participants
                },
                onRequestParticipantInfo = { userId ->
                    var user: User

                    runBlocking {
                        user = firebaseViewModel.getUserData(userId) ?: User()
                        user = user.copy(id = userId)
                    }

                    user
                },
                onRequestParticipantTrainingInfo = { userId ->
                    var userTrainingData: Training
                    runBlocking {
                        userTrainingData = firebaseViewModel.getTrainingData(
                            training[indexOfChosenTraining].id,
                            userId
                        ) ?: Training()

                    }
                    userTrainingData

                },
                onBackClick = {
                    navController.navigateUp()
                },
//                onTrainerClick = { trainerId ->
//                    firebaseViewModel.viewModelScope.launch {
//                        firebaseViewModel.chooseUser(trainerId)
//                    }
//                    navController.navigate(Screens.USER_PROFILE.name)
//                },
                onParticipantClick = { participantId ->
                    firebaseViewModel.viewModelScope.launch {
                        firebaseViewModel.chooseUser(participantId)
                    }
                    //navController.navigate(Screens.USER_PROFILE.name)
                },
                chosenParticipant = chosenUser.value,
                chosenParticipantCompletedTrainings = chosenUserCompletedTrainings.value,
                chosenParticipantFollowing = chosenUserFollowing.value,
                loggedInUser = firebaseSharedUser,
                loggedInUserfollowedUsersList = sharedSignedInUserFollowing,
                onUnFollowButtonClick = { userId ->
                    firebaseViewModel.viewModelScope.launch {
                        if (firebaseViewModel.removeUserFromFollowing(userId)) {
                            Toast.makeText(
                                navController.context,
                                "Pouzivatel bol odstraneny zo zoznamu sledovanich",
                                Toast.LENGTH_SHORT
                            ).show()
                            firebaseViewModel.getCurrentUserDataFromFirebase()
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
                            firebaseViewModel.getCurrentUserDataFromFirebase()
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
                            firebaseViewModel.getCurrentUserDataFromFirebase()
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
                                "Pouzivatel bol odstraneny zo zoznamu sledovanich",
                                Toast.LENGTH_SHORT
                            ).show()
                            firebaseViewModel.getCurrentUserDataFromFirebase()
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
                    firebaseViewModel.viewModelScope.launch {
                        val grTrainingId = firebaseViewModel.uploadGroupTrainingData(groupTraining)
                        if (grTrainingId.isNotEmpty()) {
                            firebaseViewModel.setMyCurrentGroupTraining(groupTraining.copy(id = grTrainingId))
                            navController.navigate(Screens.GROUP_TRAINING_LOBBY.name) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        composable(Screens.GROUP_TRAINING_LOBBY.name) {

            val chosenGroupTraining by firebaseViewModel.chosenGroupTrainingState.collectAsStateWithLifecycle()
            val currentLoggedInUser by firebaseViewModel.sharedUserState.collectAsStateWithLifecycle()
            val sharedSignedInUserFollowing by firebaseViewModel.sharedUserFollowingState.collectAsStateWithLifecycle()

            val chosenGroupTrainingParticipants by firebaseViewModel.chosenGroupTrainingParticipantsState.collectAsStateWithLifecycle()

            val chosenUser = firebaseViewModel.chosenUserState.collectAsStateWithLifecycle()
            val chosenUserCompletedTrainings =
                firebaseViewModel.chosenUserTrainingsState.collectAsStateWithLifecycle()
            val chosenUserFollowing =
                firebaseViewModel.chosenUserFollowingState.collectAsStateWithLifecycle()

            GroupTrainingLobby(
                chosenGroupTraining = chosenGroupTraining,
                onDeleteClick = { isListOfParticipantsEmpty ->
                    if (isListOfParticipantsEmpty) {
                        firebaseViewModel.viewModelScope.launch {
                            if (firebaseViewModel.removeGroupTraining(chosenGroupTraining.id)) {
                                Toast.makeText(
                                    navController.context,
                                    "Skupinovy trening bol odstraneny",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate(Screens.HOME.name) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            navController.context,
                            "Pre odstranenie treningu sa musia vsetci ucastnici odpojit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                },
                currentUser = currentLoggedInUser,
                onEndGroupTrainingClick = { groupTraining ->

                    if (groupTraining.trainingDuration < 5000) {
                        firebaseViewModel.viewModelScope.launch {
                            firebaseViewModel.removeGroupTraining(groupTraining.id)
                        }
                        Toast.makeText(
                            navController.context,
                            "Tréning nebol uložený, pretože trval menej ako 5 sekúnd",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        firebaseViewModel.viewModelScope.launch {
                            if (firebaseViewModel.uploadGroupTrainingData(
                                    groupTraining,
                                    groupTraining.id
                                ).isNotEmpty() && firebaseViewModel.uploadLoggedInUserTrainingData(
                                    groupTraining.trainingIndex,
                                    trainingList[groupTraining.trainingIndex].copy(
                                        trainingDuration = groupTraining.trainingDuration,
                                        timeDateOfTraining = groupTraining.timeDateOfTraining,
                                        isGroupTraining = true,
                                        id = groupTraining.id
                                    )
                                )
                            ) {
                                Toast.makeText(
                                    navController.context,
                                    "Skupinovy trening sa uspesne ulozil",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {
                                Toast.makeText(
                                    navController.context,
                                    "Nastala chyba pri ukladani udajov",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    navController.navigate(Screens.HOME.name) {
                        popUpTo(0) { inclusive = true }
                    }


                },
                allTrainingParticipants = chosenGroupTrainingParticipants,
                onCheckAllTrainingInfo = {
                    runBlocking {
                        firebaseViewModel.fetchGroupTrainingData(chosenGroupTraining.id)
                        firebaseViewModel.fetchAllParticipantsOfTraining(chosenGroupTraining.id)
                    }
                },
                setTrainingState = { state ->
                    firebaseViewModel.viewModelScope.launch {
                        firebaseViewModel.setGroupTrainingState(chosenGroupTraining.id, state)
                    }
                },
                onSendAllUsers = { indexOfTraining ->
                    if (currentLoggedInUser.id != chosenGroupTraining.trainerId) {
                        navController.navigate("${Screens.CURRENT_ACTIVITY.name}/$indexOfTraining") {
                            popUpTo(0) { inclusive = true }
                        }
                    } else {
                        firebaseViewModel.viewModelScope.launch {
                            firebaseViewModel.createLoggedInUserTraining(
                                indexOfTraining,
                                chosenGroupTraining.id
                            )
                        }
                    }
                },
                onRemoveUserFromTrainingClick = { participantId ->
                    firebaseViewModel.viewModelScope.launch {
                        firebaseViewModel.removeUserFromGroupTraining(
                            userId = participantId,
                            groupTrainingId = chosenGroupTraining.id
                        )
                        firebaseViewModel.fetchAllParticipantsOfTraining(chosenGroupTraining.id)
                    }
                },
                onSendUserToHomeScreen = {
//                    Toast.makeText(
//                        navController.context,
//                        "Bol si vyhodeny z treningu!",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    navController.navigate(Screens.HOME.name) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onUserClick = { chosenUserId ->
                    firebaseViewModel.viewModelScope.launch {
                        firebaseViewModel.chooseUser(chosenUserId)
                    }
                },
                loggedInUserfollowedUsersList = sharedSignedInUserFollowing,
                chosenUser = chosenUser.value,
                chosenUserCompletedTrainings = chosenUserCompletedTrainings.value,
                chosenUserFollowing = chosenUserFollowing.value,
                onUnFollowButtonClick = { userId ->
                    firebaseViewModel.viewModelScope.launch {
                        if (firebaseViewModel.removeUserFromFollowing(userId)) {
                            Toast.makeText(
                                navController.context,
                                "Pouzivatel bol odstraneny zo zoznamu sledovanich",
                                Toast.LENGTH_SHORT
                            ).show()
                            firebaseViewModel.getCurrentUserDataFromFirebase()
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
                            firebaseViewModel.getCurrentUserDataFromFirebase()
                        } else {
                            Toast.makeText(
                                navController.context,
                                "Nastala chyba pri ukladani udajov",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                isBLEConnected = bleConnectionState
            )
        }

        composable(Screens.QR_READER_SCREEN.name) {

            val foundGroupTraining = remember { mutableStateOf(GroupTraining()) }
            val foundTrainer = remember { mutableStateOf(User()) }

            QrReaderScreen(

                onResult = { groupTrainingIdResult ->
                    firebaseViewModel.viewModelScope.launch {
                        foundGroupTraining.value =
                            firebaseViewModel.getGroupTrainingData(groupTrainingIdResult)
                                ?: GroupTraining()
                        foundTrainer.value =
                            firebaseViewModel.getUserData(foundGroupTraining.value.trainerId)
                                ?: User()
                    }
                },
                foundGroupTraining = foundGroupTraining.value,
                foundTrainer = foundTrainer.value,
                onConfirmation = { groupTrainingId ->
                    if (bleConnectionState > 1) {
                        firebaseViewModel.viewModelScope.launch {
                            if (firebaseViewModel.addCurrentUserToGroupTraining(groupTrainingId)) {
                                firebaseViewModel.setMyCurrentGroupTraining(
                                    firebaseViewModel.getGroupTrainingData(
                                        groupTrainingId
                                    ) ?: GroupTraining()
                                )
                                firebaseViewModel.fetchAllParticipantsOfTraining(groupTrainingId)
                                navController.navigate(Screens.GROUP_TRAINING_LOBBY.name) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            navController.context,
                            "Skontroluj pripojenie zariadenia",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }

        composable(Screens.DEVICE_SCREEN.name) {

            DeviceScreen(
                bleClient = bleClient,
                onBackClick = {
                    navController.navigateUp()
                },
                onDisconnectClick = {
                    Toast.makeText(
                        navController.context,
                        "Zariadenie je odpojene",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}

