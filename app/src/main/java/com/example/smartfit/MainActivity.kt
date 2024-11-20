package com.example.smartfit


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.smartfit.navigation.AppNavigator
import com.example.smartfit.ui.theme.SmartFitTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            SmartFitTheme {

                AppNavigator()
            }
        }
    }
}