package com.example.smartfit

import android.app.Application
import androidx.compose.ui.res.stringResource
import com.google.firebase.FirebaseApp
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider

class FirebaseInit : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

    }

}