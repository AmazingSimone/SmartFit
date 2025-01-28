package com.example.smartfit


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartfit.ble_device.BLEClient
import com.example.smartfit.firebase.signin.SharedFirebaseViewModel
import com.example.smartfit.navigation.AppNavigator
import com.example.smartfit.ui.theme.SmartFitTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)

    private lateinit var bleClient: BLEClient


    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        setContent {
            val firebaseViewModel = viewModel<SharedFirebaseViewModel>()
            bleClient = BLEClient(this, firebaseViewModel)
            SmartFitTheme {

                MultiplePermissionHandler()

                AppNavigator(bleClient = bleClient, firebaseViewModel = firebaseViewModel)

            }
        }
    }
}

