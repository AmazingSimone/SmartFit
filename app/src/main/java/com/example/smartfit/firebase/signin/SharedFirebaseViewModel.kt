package com.example.smartfit.firebase.signin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfit.data.Training
import com.example.smartfit.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SharedFirebaseViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _sharedUserState = MutableStateFlow(User("", "", ""))
    val sharedUserState = _sharedUserState.asStateFlow()

    private val _isLoadingUserData = MutableStateFlow(false)
    val isLoading = _isLoadingUserData.onStart {
        checkCurrentUser()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        true
    )

    // --- FIREBASEAUTH

    fun checkCurrentUser() {

        viewModelScope.launch {
            _sharedUserState.value = getUserData(firebaseAuth.currentUser?.uid ?: "") ?: User()

        }
    }


    fun getInstance(): FirebaseAuth {
        return firebaseAuth
    }

    fun isSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun signOut() {
        firebaseAuth.signOut()
        _sharedUserState.value = User()
    }

    // --- FIRESTORE

    suspend fun doesUserExist(id: String): Boolean {
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
                    "isTrainer" to false
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
                displayName = data?.get("displayName") as String ?: "",
                profilePicUrl = data["profilePicUrl"] as String,
                birthDate = data["birthDate"] as Long,
                height = (data["height"] as Number).toFloat(),
                weight = (data["weight"] as Number).toFloat(),
                bio = data["bio"] as String,
                color = (data["color"] as Number).toInt(),
                isTrainer = data["isTrainer"] as Boolean
            )
        } else {
            null
        }
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
                    "isTrainer" to user.isTrainer
                )
            ).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun uploadTrainingData(indexOfTraining: Int, training: Training): Boolean {
        return try {
            firebaseFirestore.collection("trainings").document().set(
                mapOf(
                    "indexOfTraining" to indexOfTraining,
                    "creatorId" to training.creatorId,
                    "trainingDuration" to training.trainingDuration,
                    "timeDateOfTraining" to training.timeDateOfTraining,
                    "avgSpeed" to training.avgSpeed,
                    "burnedCalories" to training.burnedCalories,
                    "avgHeartRate" to training.avgHeartRate,
                    "avgTempo" to training.avgTempo,
                    "steps" to training.steps,
                    "trainingTemperature" to training.trainingTemperature
                )
            ).await()
            true
        } catch (e: Exception) {
            false
        }
    }

}