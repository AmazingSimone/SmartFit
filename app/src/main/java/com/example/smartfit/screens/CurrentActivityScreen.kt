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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.smartfit.data.GroupTraining
import com.example.smartfit.data.Training
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneOffset

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentActivityScreen(
    chosenTraining: Training,
    chosenGroupTraining: GroupTraining? = null,
    onCheckAllTrainingInfo: () -> Unit = {},
    onEndTraining: (Training) -> Unit,
    //onGroupTrainingEnd: (Training) -> Unit,
) {

    val training = remember { mutableStateOf(chosenTraining) }
    val stopWatch = remember { StopWatch() }
    val isRunning = remember { mutableStateOf(stopWatch.isRunning()) }
    val timeDateOfTraining = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)

    val groupTraining = remember { mutableStateOf(chosenGroupTraining) }

    //TODO Skus lepsie spravit
    if (chosenGroupTraining != null) {
        LaunchedEffect(chosenGroupTraining.id) {
            while (true) {
                Log.d("ahoj", "vnutri while")
                withContext(Dispatchers.IO) {
                    onCheckAllTrainingInfo()
                    Log.d("ahoj", "$groupTraining")
                }
                //delay(1000)
            }
        }

        LaunchedEffect(chosenGroupTraining.trainingState) {
            when (chosenGroupTraining.trainingState) {
                0 -> {
                    stopWatch.start()
                    isRunning.value = stopWatch.isRunning()
                }

                1 -> {
                    stopWatch.start()
                    isRunning.value = stopWatch.isRunning()
                }

                2 -> {
                    stopWatch.pause()
                    isRunning.value = stopWatch.isRunning()
                }

                3 -> {
                    stopWatch.start()
                    isRunning.value = stopWatch.isRunning()
                }

                4 -> {
                    stopWatch.pause()
                    onCheckAllTrainingInfo()
                    training.value =
                        training.value.copy(trainingDuration = stopWatch.getTimeMillis())
                    training.value =
                        training.value.copy(
                            timeDateOfTraining = groupTraining.value?.timeDateOfTraining
                                ?: 0
                        )
                    training.value = training.value.copy(isGroupTraining = true)
                    training.value = training.value.copy(id = groupTraining.value?.id ?: "")

                    onEndTraining(training.value)
                }
            }
        }
    }




    Scaffold(
        topBar = {
            TopAppBar(
                title = { Heading1(training.value.name) }
            )
        },
        bottomBar = {
            if ((groupTraining.value?.trainingState ?: 0) == 0) {
                Row() {
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
                            training.value =
                                training.value.copy(trainingDuration = stopWatch.getTimeMillis())
                            training.value =
                                training.value.copy(timeDateOfTraining = timeDateOfTraining)
                            onEndTraining(training.value)

                        },
                        containerColor = MaterialTheme.colorScheme.error,
                        buttonText = "Dokoncit"
                    )

                }
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
        Training("Beh", Icons.Default.DirectionsRun),
        onEndTraining = {}
    )
}