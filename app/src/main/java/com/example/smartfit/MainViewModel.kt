package com.example.smartfit

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfit.components.StopWatch
import com.example.smartfit.data.GroupTraining
import com.example.smartfit.data.NrfData
import com.example.smartfit.data.Training
import com.example.smartfit.data.User
import com.example.smartfit.data.trainingList
import com.example.smartfit.influx.InfluxClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class MainViewModel : ViewModel() {

    //--INSTANCES

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val stopWatch = StopWatch()
    private val influxDBClientKotlin = InfluxClient()

    //--LOGGED IN USER

    private val _sharedUserState = MutableStateFlow(User("", "", ""))
    val sharedUserState = _sharedUserState.asStateFlow()

    private val _sharedUserTrainingsState = MutableStateFlow<List<Training>>(emptyList())
    val sharedUserTrainingsState = _sharedUserTrainingsState.asStateFlow()

    private val _sharedUserFollowingState = MutableStateFlow<List<User>>(emptyList())
    val sharedUserFollowingState = _sharedUserFollowingState.asStateFlow()

    //--CHOSEN SPECIFIC USER

    private val _chosenUserState = MutableStateFlow(User("", "", ""))
    val chosenUserState = _chosenUserState.asStateFlow()

    private val _chosenUserTrainingsState = MutableStateFlow<List<Training>>(emptyList())
    val chosenUserTrainingsState = _chosenUserTrainingsState.asStateFlow()

    private val _chosenUserFollowingState = MutableStateFlow<List<User>>(emptyList())
    val chosenUserFollowingState = _chosenUserFollowingState.asStateFlow()

    //-- GROUP TRAINING

    private val _chosenGroupTrainingState = MutableStateFlow(GroupTraining())
    val chosenGroupTrainingState = _chosenGroupTrainingState.asStateFlow()

    private val _chosenGroupTrainingParticipantsState = MutableStateFlow<List<User>>(emptyList())
    val chosenGroupTrainingParticipantsState = _chosenGroupTrainingParticipantsState.asStateFlow()

    //-- GROUP PARTICIPANTS SUMMARY

    private val _chosenGroupTrainingTrainer = MutableStateFlow(User())
    val chosenGroupTrainingTrainer = _chosenGroupTrainingTrainer.asStateFlow()

    private val _chosenGroupTrainingParticipants = MutableStateFlow<List<User>>(emptyList())
    val chosenGroupTrainingParticipants = _chosenGroupTrainingParticipants.asStateFlow()

    private val _chosenGroupTrainingParticipantsTrainingInfo =
        MutableStateFlow<List<Training>>(emptyList())
    val chosenGroupTrainingParticipantsTrainingInfo =
        _chosenGroupTrainingParticipantsTrainingInfo.asStateFlow()

    //-- TRAINING

    private val _chosenTrainingState = MutableStateFlow(Training())
    val chosenTrainingState = _chosenTrainingState.asStateFlow()

    //--

    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    //--INIT LOAD USER DATA

    private val _isLoadingUserData = MutableStateFlow(false)
    val isLoading = _isLoadingUserData.onStart {
        _isLoadingUserData.value = true
        getCurrentUserDataFromFirebase()
        _isLoadingUserData.value = false

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )

    //--BLE DEVICE STATE
    private val _bleState = MutableStateFlow(0) // Initial state
    val bleState = _bleState.asStateFlow()

    private val _bleConnectedDevice = MutableStateFlow<BluetoothDevice?>(null) // Initial state
    val bleConnectedDevice = _bleConnectedDevice.asStateFlow()

    private val _bleListOfDevices = MutableStateFlow<List<ScanResult>?>(null)
    val bleListOfDevices = _bleListOfDevices.asStateFlow()

    private val _bleData = MutableStateFlow(NrfData())
    val bleData = _bleData.asStateFlow()

    //-- STOPWATCH

    private val _stopWatchState = MutableStateFlow(stopWatch)
    val stopWatchState = _stopWatchState.asStateFlow()

    //-- INFLUX

    private val _influxClientState = MutableStateFlow(influxDBClientKotlin)
    val influxClientState = _influxClientState.asStateFlow()

    fun getCurrentUserDataFromFirebase() {

        viewModelScope.launch {
            _sharedUserState.value = getUserData(firebaseAuth.currentUser?.uid ?: "") ?: User()
            _sharedUserTrainingsState.value =
                getAllUserTrainings(firebaseAuth.currentUser?.uid ?: "")
            _sharedUserFollowingState.value =
                getAllUserFollowing(firebaseAuth.currentUser?.uid ?: "")

        }
    }


//    fun checkChosenUser(userId: String) {
//
//        viewModelScope.launch {
//            _chosenUserState.value = getUserData(userId) ?: User()
//            _chosenUserTrainingsState.value = getAllUserTrainings(userId)
//            _chosenUserFollowingState.value = getAllUserFollowing(userId)
//        }
//    }

    // --- FIREBASEAUTH

    fun isSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun signOut() {
        firebaseAuth.signOut()
        _sharedUserState.value = User()
        _sharedUserTrainingsState.value = emptyList()
    }

    // --- FIRESTORE

    private suspend fun doesUserExist(id: String): Boolean {
        return firebaseFirestore.collection("users").document(id).get().await().exists()
    }

    suspend fun createUserIfNotExists(userId: String) {
        if (!doesUserExist(userId)) {
            firebaseFirestore.collection("users").document(userId).set(
                mapOf(
                    "displayName" to firebaseAuth.currentUser?.displayName,
                    "profilePicUrl" to firebaseAuth.currentUser?.photoUrl,
                    "birthDate" to 0L,
                    "height" to 0f,
                    "weight" to 0f,
                    "bio" to "",
                    "color" to Color.Unspecified.toArgb(),
                    "isTrainer" to false,
                    "activityGoal" to "90",
                    "stepsGoal" to "10000",
                    "caloriesGoal" to "500"

                )
            ).await()
        } else {
            firebaseFirestore.collection("users").document(userId).update(
                mapOf(
                    "displayName" to firebaseAuth.currentUser?.displayName,
                    "profilePicUrl" to firebaseAuth.currentUser?.photoUrl
                )
            )
        }
    }

    suspend fun getUserData(userId: String): User? {
        if (userId.isEmpty()) {
            return null
        }
        val documentSnapshot = firebaseFirestore.collection("users").document(userId).get().await()
        return if (documentSnapshot.exists()) {
            val data = documentSnapshot.data
            User(
                id = userId,
                displayName = data?.get("displayName") as String,
                profilePicUrl = data["profilePicUrl"] as String,
                birthDate = data["birthDate"] as Long,
                height = (data["height"] as Number).toFloat(),
                weight = (data["weight"] as Number).toFloat(),
                bio = data["bio"] as String,
                color = (data["color"] as Number).toInt(),
                isTrainer = data["isTrainer"] as Boolean,
                activityGoal = data["activityGoal"] as String,
                stepsGoal = data["stepsGoal"] as String,
                caloriesGoal = data["caloriesGoal"] as String
            )
        } else {
            null
        }
    }

    suspend fun chooseUser(userId: String) {

        _chosenUserState.value = getUserData(userId) ?: User()
        _chosenUserTrainingsState.value = getAllUserTrainings(userId)
        _chosenUserFollowingState.value = getAllUserFollowing(userId)

    }

    fun resetChosenUser() {
        _chosenUserState.value = User()
        _chosenUserTrainingsState.value = emptyList()
        _chosenUserFollowingState.value = emptyList()
    }

    suspend fun uploadUserData(user: User): Boolean {
        return try {
            firebaseFirestore.collection("users").document(user.id).update(
                mapOf(
                    "displayName" to user.displayName,
                    "profilePicUrl" to user.profilePicUrl,
                    "birthDate" to user.birthDate,
                    "height" to user.height,
                    "weight" to user.weight,
                    "bio" to user.bio,
                    "color" to user.color,
                    "isTrainer" to user.isTrainer,
                    "activityGoal" to user.activityGoal,
                    "stepsGoal" to user.stepsGoal,
                    "caloriesGoal" to user.caloriesGoal
                )
            ).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun createLoggedInUserTraining(indexOfTraining: Int, groupTrainingId: String): String {
        val training = Training()
        return try {
            val documentReference = if (groupTrainingId.isEmpty()) {
                firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid ?: "")
                    .collection("trainings").document()
            } else {
                firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid ?: "")
                    .collection("trainings").document(groupTrainingId)
            }
            documentReference.set(
                mapOf(
                    "indexOfTraining" to indexOfTraining,
                    "trainingDuration" to training.trainingDuration,
                    "timeDateOfTraining" to training.timeDateOfTraining,
                    //"avgSpeed" to training.avgSpeed,
                    "burnedCalories" to training.burnedCalories,
                    //"avgHeartRate" to training.avgHeartRate,
                    //"avgTempo" to training.avgTempo,
                    "steps" to training.steps,
                    //"trainingTemperature" to training.trainingTemperature,
                    "isGroupTraining" to training.isGroupTraining,
                    "id" to documentReference.id
                )
            ).await()
            _chosenTrainingState.value = training.copy(id = documentReference.id)
            documentReference.id
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun uploadLoggedInUserTrainingData(training: Training): Boolean {
        return try {
            val documentReference =
                firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid ?: "")
                    .collection("trainings").document(training.id)

            documentReference.update(
                    mapOf(
                        "trainingDuration" to training.trainingDuration,
                        "timeDateOfTraining" to training.timeDateOfTraining,
                        "burnedCalories" to training.burnedCalories,
                        "steps" to training.steps,
                        "isGroupTraining" to training.isGroupTraining,
                    )
                ).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun getTrainingData(trainingId: String, userId: String): Training? {
        //val firestore = FirebaseFirestore.getInstance()
        val documentSnapshot =
            firebaseFirestore.collection("users").document(userId).collection("trainings")
                .document(trainingId).get().await()
        return if (documentSnapshot.exists()) {

            val data = documentSnapshot.data

            Training(
                name = trainingList[(data?.get("indexOfTraining") as? Number)?.toInt() ?: 0].name,
                icon = trainingList[(data?.get("indexOfTraining") as? Number)?.toInt() ?: 0].icon,
                //creatorId = data?.get("creatorId") as? String ?: "",
                trainingDuration = (data?.get("trainingDuration") as? Number)?.toLong() ?: 0L,
                timeDateOfTraining = (data?.get("timeDateOfTraining") as? Number)?.toLong() ?: 0L,
                //avgSpeed = (data?.get("avgSpeed") as? Number)?.toFloat() ?: 0f,
                burnedCalories = (data?.get("burnedCalories") as? Number)?.toInt() ?: 0,
                //avgHeartRate = (data?.get("avgHeartRate") as? Number)?.toInt() ?: 0,
                //avgTempo = (data?.get("avgTempo") as? Number)?.toInt() ?: 0,
                steps = (data?.get("steps") as? Number)?.toInt() ?: 0,
                //trainingTemperature = (data?.get("trainingTemperature") as? Number)?.toInt() ?: 0,
                isGroupTraining = data?.get("isGroupTraining") as? Boolean ?: false,
                id = trainingId
            )
        } else {
            null
        }
    }

    private suspend fun getAllUserTrainings(userId: String): List<Training> {
        if (userId == "") return emptyList()
        val trainings = mutableListOf<Training>()
        val documents =
            firebaseFirestore.collection("users").document(userId).collection("trainings").get()
                .await()


        if (!documents.isEmpty) {

            for (document in documents) {

                val trainingId = document.id

                val training = getTrainingData(trainingId, userId)

                if (training != null) {
                    trainings.add(training)

                }
            }
        } else {
            return emptyList()
        }
        return trainings
    }

    suspend fun addUserToFollowing(userId: String): Boolean {
        return try {
            val currentUserId = firebaseAuth.currentUser?.uid ?: return false
            firebaseFirestore.collection("users").document(currentUserId)
                .collection("followedUsers").document(userId).set(
                    mapOf("userReference" to firebaseFirestore.collection("users").document(userId))
                ).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun removeUserFromFollowing(userId: String): Boolean {
        return try {
            val currentUserId = firebaseAuth.currentUser?.uid ?: return false
            firebaseFirestore.collection("users").document(currentUserId)
                .collection("followedUsers").document(userId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun getAllUserFollowing(userId: String): List<User> {
        if (userId == "") return emptyList()
        val followedUsers = mutableListOf<User>()
        val documents =
            firebaseFirestore.collection("users").document(userId).collection("followedUsers").get()
                .await()


        if (!documents.isEmpty) {

            for (document in documents) {

                val followedUserId = document.id

                val followedUser = getUserData(followedUserId)

                if (followedUser != null) {
                    followedUsers.add(followedUser)

                }
            }
        } else {
            return emptyList()
        }
        return followedUsers
    }

    //TODO Mozno daky znak ze sa loaduje by bolo treba
    suspend fun searchUsers(query: String) {
        val users = mutableListOf<User>()
        if (query.isNotEmpty()) {
            val documents = firebaseFirestore.collection("users")
                .whereGreaterThanOrEqualTo("displayName", query)
                .whereLessThanOrEqualTo("displayName", query + "\uf8ff")
                .get()
                .await()

            for (document in documents) {
                val userId = document.id
                val user = getUserData(userId)
                if (user != null) {
                    users.add(user)
                }
            }
            _searchResults.value = users
        } else {
            _searchResults.value = emptyList()
        }
    }

    suspend fun uploadGroupTrainingData(
        groupTraining: GroupTraining,
        groupTrainingId: String = ""
    ): String {
        return try {
            val documentReference = if (groupTrainingId.isEmpty()) {
                firebaseFirestore.collection("groupTrainings").document()
            } else {
                firebaseFirestore.collection("groupTrainings").document(groupTrainingId)
            }
            documentReference.set(
                mapOf(
                    "trainerId" to groupTraining.trainerId,
                    "name" to groupTraining.name,
                    "trainingIndex" to groupTraining.trainingIndex,
                    "maxUsers" to groupTraining.maxUsers,
                    "trainingDuration" to groupTraining.trainingDuration,
                    "timeDateOfTraining" to groupTraining.timeDateOfTraining,
                    "trainingDetails" to groupTraining.trainingDetails,
                    "trainingState" to groupTraining.trainingState
                )
            ).await()
            documentReference.id
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun removeGroupTraining(groupTrainingId: String): Boolean {

        return try {
            firebaseFirestore.collection("groupTrainings").document(groupTrainingId).delete()
                .await()
            true
        } catch (e: Exception) {
            false
        }

    }

    suspend fun setGroupTrainingData(groupTrainingId: String) {

        _chosenGroupTrainingTrainer.value =
            getUserData(getGroupTrainingData(groupTrainingId)?.trainerId.toString()) ?: User()
        _chosenGroupTrainingParticipants.value = getParticipantsOfGroupTraining(groupTrainingId)
        _chosenGroupTrainingParticipantsTrainingInfo.value =
            getParticipantsOfGroupTrainingData(groupTrainingId)

    }

    suspend fun getGroupTrainingData(groupTrainingId: String): GroupTraining? {
        return try {
            val documentSnapshot =
                firebaseFirestore.collection("groupTrainings").document(groupTrainingId).get()
                    .await()
            if (documentSnapshot.exists()) {
                val data = documentSnapshot.data
                val participantsCount = try {
                    firebaseFirestore.collection("groupTrainings").document(groupTrainingId)
                        .collection("participants").get().await().size()
                } catch (e: Exception) {
                    0
                }
                GroupTraining(
                    id = groupTrainingId,
                    trainerId = data?.get("trainerId") as String,
                    name = data["name"] as String,
                    trainingIndex = (data["trainingIndex"] as Number).toInt(),
                    maxUsers = (data["maxUsers"] as Number).toInt(),
                    trainingDuration = (data["trainingDuration"] as Number).toLong(),
                    timeDateOfTraining = (data["timeDateOfTraining"] as Number).toLong(),
                    numberOfParticipants = participantsCount,
                    trainingDetails = data["trainingDetails"] as String,
                    trainingState = (data["trainingState"] as Number).toInt()
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchGroupTrainingData(groupTrainingId: String) {
        _chosenGroupTrainingState.value = getGroupTrainingData(groupTrainingId) ?: GroupTraining()
    }

    suspend fun addCurrentUserToGroupTraining(groupTrainingId: String): Boolean {
        return try {
            val currentUserId = firebaseAuth.currentUser?.uid ?: return false
            firebaseFirestore.collection("groupTrainings").document(groupTrainingId)
                .collection("participants").document(currentUserId).set(
                    mapOf(
                        "userReference" to firebaseFirestore.collection("users")
                            .document(currentUserId)
                    )
                ).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun removeUserFromGroupTraining(userId: String, groupTrainingId: String): Boolean {
        return try {
            firebaseFirestore.collection("groupTrainings").document(groupTrainingId)
                .collection("participants").document(userId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun setMyCurrentGroupTraining(groupTraining: GroupTraining) {

        _chosenGroupTrainingState.value = groupTraining

    }

    suspend fun fetchAllParticipantsOfTraining(groupTrainingId: String) {
        try {
            val documents = firebaseFirestore.collection("groupTrainings")
                .document(groupTrainingId)
                .collection("participants")
                .get()
                .await()

            if (documents.isEmpty) {
                _chosenGroupTrainingParticipantsState.value = emptyList()
            } else {
                val participants = documents.mapNotNull { document ->
                    val userId = document.id
                    getUserData(userId)
                }
                _chosenGroupTrainingParticipantsState.value = participants
            }
        } catch (e: Exception) {
            _chosenGroupTrainingParticipantsState.value = emptyList()
        }
    }

    suspend fun setGroupTrainingState(groupTrainingId: String, state: Int): Boolean {
        return try {
            firebaseFirestore.collection("groupTrainings").document(groupTrainingId)
                .update("trainingState", state).await()
//            _chosenGroupTrainingState.value =
//                getGroupTrainingData(groupTrainingId) ?: GroupTraining()
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun getParticipantsOfGroupTraining(groupTrainingId: String): List<User> {
        return try {

            val documents = firebaseFirestore.collection("groupTrainings")
                .document(groupTrainingId)
                .collection("participants")
                .get()
                .await()

            val participants = mutableListOf<User>()
            for (document in documents) {
                val userId = document.id
                getUserData(userId)?.let { participants.add(it) }
            }
            participants
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun getParticipantsOfGroupTrainingData(groupTrainingId: String): List<Training> {
        return try {

            val documents = firebaseFirestore.collection("groupTrainings")
                .document(groupTrainingId)
                .collection("participants")
                .get()
                .await()

            val groupTrainings = mutableListOf<Training>()
            for (document in documents) {
                val userId = document.id
                getTrainingData(groupTrainingId, userId)?.let { groupTrainings.add(it) }
            }
            groupTrainings
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun setBleState(state: Int) {
        _bleState.value = state
        Log.d("AHOJBLE", "BLE state updated to ${bleState.value}")
    }

    fun setListOfDevices(listOfDevices: List<ScanResult>) {
        _bleListOfDevices.value = listOfDevices
        Log.d("AHOJBLE", "BLE list of devices updated to ${bleListOfDevices.value?.size}")
    }

    fun setBleData(bleData: NrfData) {
        _bleData.value = bleData
    }

    fun setBleConnectedDevice(bleDevice: BluetoothDevice?) {
        _bleConnectedDevice.value = bleDevice
    }
}

