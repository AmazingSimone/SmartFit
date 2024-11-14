package com.example.smartfit.firebase.sign_in

import androidx.compose.ui.res.stringResource
import com.example.smartfit.R
import com.example.smartfit.data.User
import com.example.smartfit.screens.FBUser
import com.google.firebase.auth.FirebaseAuth
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider

class OneTapSignIn {

    private val firebaseAuth = FirebaseAuth.getInstance()
    //private val googleAuthProvider = GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = stringResource(R.string.default_web_client_id)))


    fun getSignedInUser(): User? {
        firebaseAuth.currentUser?.run {
            return User(
                id = uid,
                displayName = displayName,
                profilePicUrl = photoUrl?.toString()
            )
        }
        return null
    }

    fun signOutUser() {
        firebaseAuth.signOut()
    }

}

