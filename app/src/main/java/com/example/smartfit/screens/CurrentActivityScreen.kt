package com.example.smartfit.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.smartfit.BuildConfig
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.RunningTrainingInfoContent
import com.example.smartfit.components.StopWatch
import com.example.smartfit.data.GroupTraining
import com.example.smartfit.data.InfluxTrainingMeasurement
import com.example.smartfit.data.NrfData
import com.example.smartfit.data.Training
import com.example.smartfit.data.User
import com.example.smartfit.data.trainingList
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentActivityScreen(
    participant: User,
    createdTraining: Training = Training(),
    indexOfTraining: Int,
    chosenGroupTraining: GroupTraining? = null,
    onCheckAllTrainingInfo: () -> Unit = {},
    onCreateTraining: (String) -> Unit,
    onEndTraining: (Training) -> Unit,
    nrfData: NrfData,
    //onGroupTrainingEnd: (Training) -> Unit,
) {

    val training = remember { mutableStateOf(createdTraining) }
    val stopWatch = remember { StopWatch() }
    val isRunning = remember { mutableStateOf(stopWatch.isRunning()) }
    val timeDateOfTraining = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)

    val groupTraining = remember { mutableStateOf(chosenGroupTraining) }

    val data = remember { mutableStateOf(nrfData) }

    val isTrainingCreated = remember { mutableStateOf(false) }

    val influxDBClientKotlin = InfluxDBClientKotlinFactory.create(
        BuildConfig.INFLUX_URL,
        BuildConfig.INFLUX_TOKEN.toCharArray(),
        BuildConfig.INFLUX_ORG,
        BuildConfig.INFLUX_BUCKET
    )


    LaunchedEffect(createdTraining) {
        training.value = createdTraining
    }

    //TODO Skus lepsie spravit
    if (chosenGroupTraining != null) {
        LaunchedEffect(chosenGroupTraining.id) {
            while ((groupTraining.value?.trainingState ?: 4) != 4) {
                withContext(Dispatchers.IO) {
                    onCheckAllTrainingInfo()
                }
                //delay(1000)
            }
        }

        LaunchedEffect(nrfData) {

            if (stopWatch.getTimeMillis() >= 5000 && !isTrainingCreated.value) {
                onCreateTraining(groupTraining.value?.id ?: "")
                isTrainingCreated.value = true
            }

            data.value = nrfData

            data.value = data.value.copy(
                rychlost = ((nrfData.vzdialenost.replace(",", ".")
                    .toDouble() / (stopWatch.getTimeMillis() / 1000)) * 3.6).toInt().toString(),
            )

            data.value = data.value.copy(
                kadencia = if (stopWatch.getTimeMillis() <= 0L) {
                    "0"
                } else {
                    val timeInMinutes = (stopWatch.getTimeMillis() / 60000.0)
                    if (timeInMinutes == 0.0) {
                        "0"
                    } else {
                        ((nrfData.kroky.toInt() / timeInMinutes)).toInt().toString()
                    }
                }
            )

            if (training.value.id.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    influxDBClientKotlin.use {
                        Log.d("AHOJ", " data ${nrfData.toString()}")
                        val writeApi = influxDBClientKotlin.getWriteKotlinApi()
                        val mem = InfluxTrainingMeasurement(
                            userId = participant.id,
                            trainingId = createdTraining.id,
                            tep = data.value.tep.toInt(),
                            teplota = data.value.teplota.replace(",", ".").toDouble(),
                            kroky = data.value.kroky.toInt(),
                            kadencia = data.value.kadencia.toInt(),
                            spaleneKalorie = data.value.spaleneKalorie.toInt(),
                            vzdialenost = data.value.vzdialenost.replace(",", ".").toInt(),
                            saturacia = data.value.saturacia.replace(",", ".").toDouble(),
                            rychlost = data.value.rychlost.toInt(),
                            time = Instant.now()
                        )
                        writeApi.writeMeasurement(mem, WritePrecision.NS)

                        Log.d("AHOJ", "influx write ${mem.toString()}")
                    }
                }
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
                    training.value =
                        training.value.copy(burnedCalories = nrfData.spaleneKalorie.toInt())
                    training.value = training.value.copy(steps = nrfData.kroky.toInt())

                    training.value = training.value.copy(isGroupTraining = true)
                    //training.value = training.value.copy(id = groupTraining.value?.id ?: "")

                    influxDBClientKotlin.close()

                    onEndTraining(training.value)
                }
            }
        }
    }




    Scaffold(
        topBar = {
            TopAppBar(
                title = { Heading1(trainingList[indexOfTraining].name) }
            )
        },
        bottomBar = {
            Row() {
                if ((groupTraining.value?.trainingState ?: 0) == 0) {
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
                            containerColor = MaterialTheme.colorScheme.tertiary,
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

                            training.value =
                                training.value.copy(burnedCalories = nrfData.spaleneKalorie.toInt())
                            training.value = training.value.copy(steps = nrfData.kroky.toInt())


                            Log.d("AHOJ", "end of training ${training.toString()}")

                            influxDBClientKotlin.close()

                            onEndTraining(training.value)

                        },
                        containerColor = MaterialTheme.colorScheme.error,
                        buttonText = "Dokoncit"
                    )

                } else {
                    Heading1("")
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

                RunningTrainingInfoContent(stopWatch, data.value)


            }
        }
    }
}

@Preview
@Composable
fun CurrentActivityPreview() {
    CurrentActivityScreen(
        createdTraining = Training("Beh", Icons.Default.DirectionsRun),
        onEndTraining = {},
        chosenGroupTraining = GroupTraining(trainingState = 0),
        onCheckAllTrainingInfo = {},
        nrfData = NrfData(),
        participant = User(),
        onCreateTraining = {},
        indexOfTraining = 0,
    )
}