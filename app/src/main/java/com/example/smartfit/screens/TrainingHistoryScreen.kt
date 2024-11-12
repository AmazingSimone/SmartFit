package com.example.smartfit.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomTrainingInfoCardWithDate
import com.example.smartfit.components.Heading1
import com.example.smartfit.data.trainings

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingHistoryScreen() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Heading1("Historia treningov") },
                actions = {
                    IconButton(onClick = { /* Handle navigation icon press */ }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                    }
                }
            )
        }

    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.TopCenter
            ) {

                Column(
                    Modifier.verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CustomTrainingInfoCardWithDate(
                        training = trainings[0]
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TrainingHistoryPreview() {
    TrainingHistoryScreen()
}