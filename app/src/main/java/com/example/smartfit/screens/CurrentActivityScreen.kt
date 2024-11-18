package com.example.smartfit.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.CustomTrainingInfoDisplayCard
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.StopWatch
import com.example.smartfit.data.Training

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentActivityScreen(
    chosenTraining: Training
) {

    val stopWatch = remember { StopWatch() }
    val isRunning = remember { mutableStateOf(stopWatch.isRunning()) }

    stopWatch.start()
    isRunning.value = stopWatch.isRunning()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Heading1(chosenTraining.name) }
            )
        },
        bottomBar = {
            Row (){
                if (isRunning.value) {
                    CustomButton(
                        modifier = Modifier
                            .padding(20.dp)
                            .weight(1f),
                        onClick = {
                            stopWatch.pause()
                            isRunning.value = stopWatch.isRunning()
                        },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        buttonText = "Pozastavit"
                    )
                } else {
                    CustomButton(
                        modifier = Modifier
                            .padding(20.dp)
                            .weight(1f),
                        onClick = {
                            stopWatch.start()
                            isRunning.value = stopWatch.isRunning()
                        },
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                        buttonText = "Sputstit"
                    )
                }

                CustomButton(
                    modifier = Modifier
                        .padding(20.dp)
                        .weight(1f),
                    onClick = {
                        stopWatch.pause()
                        Log.d("timeMillis","${stopWatch.getTimeMillis()}")

                    },
                    containerColor = MaterialTheme.colorScheme.error,
                    buttonText = "Dokoncit"
                )

            }
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

                ) {
                    CustomTrainingInfoDisplayCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(50.dp),
                        title = "Cas",
                        timeData = stopWatch.getCustomFormattedTime()
                    )
                    Spacer(Modifier.padding(5.dp))
                    Row {
                        CustomTrainingInfoDisplayCard(
                            modifier = Modifier
                                .fillMaxWidth(0.48f)
                                .padding(30.dp),
                            title = "Vzdialenost"
                        )
                        Spacer(Modifier.padding(5.dp))

                        CustomTrainingInfoDisplayCard(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(30.dp),

                            title = "Srdcovy tep"
                        )
                    }
                    Spacer(Modifier.padding(5.dp))
                    Row {
                        CustomTrainingInfoDisplayCard(
                            modifier = Modifier
                                .fillMaxWidth(0.48f)
                                .padding(30.dp),

                            title = "Kadencia"
                        )
                        Spacer(Modifier.padding(5.dp))

                        CustomTrainingInfoDisplayCard(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(30.dp),

                            title = "Rychlost"
                        )
                    }
                    Spacer(Modifier.padding(5.dp))
                    Row {
                        CustomTrainingInfoDisplayCard(
                            modifier = Modifier
                                .fillMaxWidth(0.48f)
                                .padding(30.dp),

                            title = "Spalene kalorie"
                        )
                        Spacer(Modifier.padding(5.dp))

                        CustomTrainingInfoDisplayCard(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(43.dp),

                            title = "Teplota"
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CurrentActivityPreview() {
    CurrentActivityScreen(
        Training("Beh", Icons.Default.DirectionsRun)
    )
}