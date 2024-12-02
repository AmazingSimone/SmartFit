package com.example.smartfit.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomGroupTrainingParticipantsDetailsCard
import com.example.smartfit.components.CustomTrainingInfoDisplayCard
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.Heading2
import com.example.smartfit.data.Training
import com.example.smartfit.data.User
import com.example.smartfit.data.trainingList
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(
    training: Training,
    trainerDetails: () -> User? = { null },
    listOfParticipantsOfGroupTraining: suspend (String) -> List<String> = { emptyList() },
    onRequestParticipantInfo: suspend (String) -> User? = { null },
    onRequestParticipantTrainingInfo: suspend (String) -> Training? = { null },
    onTrainerClick: (String) -> Unit,
    onParticipantClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val participants = remember { mutableStateOf(emptyList<String>()) }
    val trainer = remember { mutableStateOf(User()) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(training.id) {
        participants.value = listOfParticipantsOfGroupTraining(training.id)
        trainer.value = trainerDetails() ?: User()
        //isLoading.value = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Heading1(training.name) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back icon"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f)
                )
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
                Column {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Heading1(
                            LocalDateTime.ofEpochSecond(
                                training.timeDateOfTraining,
                                0,
                                ZoneOffset.UTC
                            ).format(
                                DateTimeFormatter.ofPattern(
                                    if (LocalDateTime.now().year != LocalDateTime.ofEpochSecond(
                                            training.timeDateOfTraining,
                                            0,
                                            ZoneOffset.UTC
                                        ).year
                                    ) "E, d.M.yyyy" else "E, d.M"
                                )
                            )
                        )
                        Heading1(
                            LocalTime.ofSecondOfDay((training.trainingDuration / 1000).toLong())
                                .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                        )
                    }

                    if (training.isGroupTraining) {

                        Heading2("Trener:")
                        if (isLoading.value) {
                            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        } else {
                            CustomGroupTrainingParticipantsDetailsCard(
                                trainer.value,
                                onCardClick = { onTrainerClick(trainer.value.id) }
                            )
                        }
                        Spacer(Modifier.padding(8.dp))

                        LazyColumn {
                            items(participants.value) { participantId ->
                                val participant = remember { mutableStateOf(User()) }
                                val participantTraining = remember { mutableStateOf(Training()) }

                                LaunchedEffect(participantId) {
                                    participant.value =
                                        onRequestParticipantInfo(participantId) ?: User()
                                    participantTraining.value =
                                        onRequestParticipantTrainingInfo(participantId)
                                            ?: Training()
                                    isLoading.value = false
                                }
                                if (isLoading.value) {
                                    Box(
                                        Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                } else {
                                    CustomGroupTrainingParticipantsDetailsCard(
                                        participant = participant.value,
                                        training = participantTraining.value,
                                        onCardClick = { onParticipantClick(participantId) }
                                    )
                                }
                                Spacer(Modifier.padding(8.dp))
                            }
                        }

                    } else {
                        CustomTrainingInfoDisplayCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(50.dp),
                            title = "Cas",
                            timeData = LocalTime.ofSecondOfDay((training.trainingDuration / 1000).toLong())
                                .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                        )
                        Spacer(Modifier.padding(5.dp))
                        Row {
                            CustomTrainingInfoDisplayCard(
                                modifier = Modifier
                                    .fillMaxWidth(0.48f)
                                    .padding(30.dp),
                                title = "Prejdena Vzdialenost"
                            )
                            Spacer(Modifier.padding(5.dp))

                            CustomTrainingInfoDisplayCard(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(30.dp),

                                title = "Priemerny Srdcovy tep"
                            )
                        }
                        Spacer(Modifier.padding(5.dp))
                        Row {
                            CustomTrainingInfoDisplayCard(
                                modifier = Modifier
                                    .fillMaxWidth(0.48f)
                                    .padding(30.dp),

                                title = "Priemerna Kadencia"
                            )
                            Spacer(Modifier.padding(5.dp))

                            CustomTrainingInfoDisplayCard(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(30.dp),

                                title = "Priemerna Rychlost"
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

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ActivityDetailPreview() {
    ActivityDetailScreen(
        onBackClick = {},
        training = trainingList[0].copy(
            id = "KmkDqINeWhmtx1ipJyIN",
            trainingDuration = 7363,
            timeDateOfTraining = 1732746897,
            isGroupTraining = true
        ),
        listOfParticipantsOfGroupTraining = { listOf("MZ6M79VA9zetdUHX4NtgRE6UDzx2") },
        onRequestParticipantInfo = {
            User(
                id = "MZ6M79VA9zetdUHX4NtgRE6UDzx2",
                displayName = "Simon Bartanus",
                profilePicUrl = "https://lh3.googleusercontent.com/a/ACg8ocIlxeLUaG-f883-a5lmUuQaqHiiaeuouQnzFf-SZFIND2HBCLf3=s96-c",
                bio = "Toto je uzastne bio",
                color = 1
            )
        },
        onRequestParticipantTrainingInfo = {
            trainingList[0].copy(
                trainingDuration = 7363,
                timeDateOfTraining = 1732746897,
                avgSpeed = 0F,
                burnedCalories = 0F,
                avgHeartRate = 0,
                avgTempo = 0,
                steps = 0,
                trainingTemperature = 0,
                isGroupTraining = true,
                id = "pYqROgvqtzWm5cYskr1Z"
            )
        },
        onTrainerClick = {},
        onParticipantClick = {}
    )
}