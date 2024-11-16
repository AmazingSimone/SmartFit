package com.example.smartfit.firebase.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfit.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
        val currentUser = firebaseAuth.currentUser
        _sharedUserState.update {
            if (currentUser != null) {
                User(
                    id = currentUser.uid,
                    displayName = currentUser.displayName,
                    profilePicUrl = currentUser.photoUrl.toString()
                )
            } else {
                User()
            }
        }
        //Log.d("errorFB", "shared v checkUser ${sharedUserState.value}")
    }


    fun getInstance(): FirebaseAuth {
        return firebaseAuth
    }

    fun isSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun signOut() {
        firebaseAuth.signOut()
        checkCurrentUser()
    }

    // --- FIRESTORE

    suspend fun doesUserExist(id: String): Boolean {
        return firebaseFirestore.collection("users").document(id).get().await().exists()
    }

    suspend fun createUserIfNotExists(userId: String) {
        if (!doesUserExist(userId)) {
            firebaseFirestore.collection("users").document(userId).set(
                mapOf(
                    "birthDate" to "",
                    "height" to "",
                    "weight" to "",
                    "bio" to ""
                )
            ).await()
        }
    }

}