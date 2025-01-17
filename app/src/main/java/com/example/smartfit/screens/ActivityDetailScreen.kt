package com.example.smartfit.screens

import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomBottomModalSheet
import com.example.smartfit.components.CustomGroupTrainingParticipantsDetailsCard
import com.example.smartfit.components.CustomTrainingInfoDisplayCard
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.Heading2
import com.example.smartfit.data.InfluxData
import com.example.smartfit.data.Training
import com.example.smartfit.data.User
import com.example.smartfit.data.trainingList
import com.example.smartfit.influx.InfluxClient
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(
    loggedInUser: User,
    loggedInUserfollowedUsersList: List<User> = emptyList(),
    training: Training,
    trainerDetails: () -> User? = { null },
    listOfParticipantsIdsOfGroupTraining: suspend (String) -> List<String> = { emptyList() },
    onRequestParticipantInfo: suspend (String) -> User? = { null },
    onRequestParticipantTrainingInfo: suspend (String) -> Training? = { null },
    //onTrainerClick: (String) -> Unit,
    onParticipantClick: (String) -> Unit,
    chosenParticipant: User = User(),
    chosenParticipantCompletedTrainings: List<Training> = emptyList(),
    chosenParticipantFollowing: List<User> = emptyList(),
    onUnFollowButtonClick: (String) -> Unit = {},
    onFollowButtonClick: (String) -> Unit = {},
    onBackClick: () -> Unit
) {

    val influxClient = remember { InfluxClient() }

    val groupTrainingParticipantsIds = remember { mutableStateOf(emptyList<String>()) }
    val trainer = remember { mutableStateOf(User()) }
    val isLoading = remember { mutableStateOf(true) }

    val userInfluxData = remember { mutableStateOf(InfluxData()) }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }


    val policy = ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)

    LaunchedEffect(Unit) {
        userInfluxData.value = influxClient.fetchInfluxData(loggedInUser.id, training.id)
        influxClient.closeConnection()
    }

    val participants = remember { mutableStateListOf<User>() }
    val participantsInfluxData = remember { mutableStateListOf<InfluxData>() }
    val participantsTrainings = remember { mutableStateListOf<Training>() }

    if (training.isGroupTraining) {


        LaunchedEffect(Unit) {
            groupTrainingParticipantsIds.value = listOfParticipantsIdsOfGroupTraining(training.id)
            trainer.value = trainerDetails() ?: User()
            //isLoading.value = false

            groupTrainingParticipantsIds.value.forEach { participantId ->
                val participant = onRequestParticipantInfo(participantId) ?: User()
                val participantTraining =
                    onRequestParticipantTrainingInfo(participantId) ?: Training()
                val participantInfluxData = influxClient.fetchInfluxData(participantId, training.id)
                participants.add(participant)
                participantsInfluxData.add(participantInfluxData)
                participantsTrainings.add(participantTraining)
            }
            influxClient.closeConnection()
            isLoading.value = false
        }
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
                                onCardClick = {
                                    //onTrainerClick(trainer.value.id)
                                    showBottomSheet = true
                                    onParticipantClick(trainer.value.id)
                                }
                            )
                        }
                        Spacer(Modifier.padding(8.dp))

                        LazyColumn {
                            itemsIndexed(participants) { index, participant ->

                                if (isLoading.value) {
                                    Box(
                                        Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                } else {
                                    CustomGroupTrainingParticipantsDetailsCard(
                                        participant = participant,
                                        influxData = participantsInfluxData[index],
                                        training = participantsTrainings[index],
                                        onCardClick = {
                                            showBottomSheet = true
                                            onParticipantClick(participant.id)
                                        }
                                    )
                                }
                                Spacer(Modifier.padding(8.dp))
                            }
                        }

                    } else {

                        Column(Modifier.fillMaxSize()) {
                            CustomTrainingInfoDisplayCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                title = "Cas",
                                data = LocalTime.ofSecondOfDay((training.trainingDuration / 1000).toLong())
                                    .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                            )
                            Spacer(Modifier.padding(5.dp))
                            Row(Modifier.weight(1f)) {
                                CustomTrainingInfoDisplayCard(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .weight(1f),
                                    title = "Prejdena Vzdialenost",
                                    data = userInfluxData.value.vzdialenost,
                                    unit = "m"
                                )
                                Spacer(Modifier.padding(5.dp))
                                CustomTrainingInfoDisplayCard(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .weight(1f),
                                    title = "Priemerny Srdcovy tep",
                                    data = "${userInfluxData.value.tepMin} - ${userInfluxData.value.tepMax}",
                                    unit = "t/m"
                                )
                            }
                            //pocet krokov za minutu
                            Spacer(Modifier.padding(5.dp))
                            Row(Modifier.weight(1f)) {
                                CustomTrainingInfoDisplayCard(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .weight(1f),
                                    title = "Priemerna Kadencia",
                                    data = userInfluxData.value.avgKadencia,
                                    unit = "kr/min"
                                )

                                Spacer(Modifier.padding(5.dp))
                                CustomTrainingInfoDisplayCard(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .weight(1f),
                                    title = "Priemerna Rychlost",
                                    data = userInfluxData.value.avgRychlost,
                                    unit = "km/h"
                                )
                            }
                            Spacer(Modifier.padding(5.dp))
                            Row(Modifier.weight(1f)) {
                                CustomTrainingInfoDisplayCard(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .weight(1f),
                                    title = "Spalene kalorie",
                                    data = userInfluxData.value.spaleneKalorie,
                                    unit = "kcal"
                                )
                                Spacer(Modifier.padding(5.dp))
                                CustomTrainingInfoDisplayCard(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .weight(1f),
                                    title = "Teplota",
                                    data = userInfluxData.value.teplota,
                                    unit = "°C"
                                )
                            }
                        }
                    }
                }
            }
        }
        if (showBottomSheet) {

            //TODO tu nemusim realne posielat plny list doslova userov ktorych sleduje a treningov staci cisla taktiez aj v lobby
            CustomBottomModalSheet(
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false },
                receivedUser = chosenParticipant,
                completedTrainingsList = chosenParticipantCompletedTrainings,
                followedUsersList = chosenParticipantFollowing,
                loggedInUser = loggedInUser,
                onUnFollowButtonClick = onUnFollowButtonClick,
                onFollowButtonClick = onFollowButtonClick,
                loggedInUserFollowedUsersList = loggedInUserfollowedUsersList
            )
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
            isGroupTraining = false
        ),
        listOfParticipantsIdsOfGroupTraining = {
            //listOf("MZ6M79VA9zetdUHX4NtgRE6UDzx2")
            emptyList()
        },
        onRequestParticipantInfo = {
            null
//            User(
//                id = "MZ6M79VA9zetdUHX4NtgRE6UDzx2",
//                displayName = "Simon Bartanus",
//                profilePicUrl = "https://lh3.googleusercontent.com/a/ACg8ocIlxeLUaG-f883-a5lmUuQaqHiiaeuouQnzFf-SZFIND2HBCLf3=s96-c",
//                bio = "Toto je uzastne bio",
//                color = 1
//            )
        },
        onRequestParticipantTrainingInfo = {
            null
//            trainingList[0].copy(
//                trainingDuration = 7363,
//                timeDateOfTraining = 1732746897,
//                avgSpeed = 0F,
//                burnedCalories = 0F,
//                avgHeartRate = 0,
//                avgTempo = 0,
//                steps = 0,
//                trainingTemperature = 0,
//                isGroupTraining = true,
//                id = "pYqROgvqtzWm5cYskr1Z"
//            )
        },
        //onTrainerClick = {},
        onParticipantClick = {},
        loggedInUser = User()
    )
}