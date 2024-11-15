package com.example.smartfit.firebase.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.smartfit.data.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SharedFirebaseViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    private val _sharedUserState = MutableStateFlow(User("", "", ""))
    val sharedUserState = _sharedUserState.asStateFlow()

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
        Log.d("errorFB", "shared v checkUser ${sharedUserState.value}")
    }


    fun getInstance(): FirebaseAuth {
        return firebaseAuth
    }

    fun isSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

}