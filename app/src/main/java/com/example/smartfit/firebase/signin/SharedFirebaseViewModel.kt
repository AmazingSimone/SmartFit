package com.example.smartfit.firebase.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfit.data.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class SharedFirebaseViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


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
    }

}