package com.example.smartfit.screens

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.CustomProfilePictureFrame
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.Heading2
import com.example.smartfit.components.NormalText
import com.example.smartfit.components.StopWatch
import com.example.smartfit.data.GroupTraining
import com.example.smartfit.data.User
import com.example.smartfit.data.frameColors
import com.example.smartfit.data.trainingList
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupTrainingLobby(
    chosenGroupTraining: GroupTraining,
    currentUser: User,
    allTrainingParticipants: List<User>,
    onCheckAllTrainingInfo: () -> Unit = {},
    setTrainingState: (Int) -> Unit, //0 - initial, 1 - start and send to screens, 2 - pause, 3 - resume, 4 - end
    onDeleteClick: () -> Unit,
    onEndGroupTrainingClick: (GroupTraining) -> Unit,
    onSendAllUsers: (Int) -> Unit
) {

    var groupTraining by remember { mutableStateOf(chosenGroupTraining) }


    val context = LocalContext.current
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val fullscreenQrState = remember { mutableStateOf(false) }

    val stopWatch = remember { StopWatch() }
    val isStopWatchRunning = remember { mutableStateOf(stopWatch.isRunning()) }


    LaunchedEffect(chosenGroupTraining.id) {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(chosenGroupTraining.id, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        qrBitmap = bitmap

        if (currentUser.id == chosenGroupTraining.trainerId) {
            fullscreenQrState.value = true
        }

        while (true) {
            withContext(Dispatchers.IO) {
                onCheckAllTrainingInfo()
            }
            //delay(1000)
        }
    }

    LaunchedEffect(chosenGroupTraining.trainingState) {
        when (chosenGroupTraining.trainingState) {
            0 -> {
                stopWatch.reset()
                isStopWatchRunning.value = stopWatch.isRunning()
            }

            1 -> {
                onSendAllUsers(chosenGroupTraining.trainingIndex)
                stopWatch.start()
                isStopWatchRunning.value = stopWatch.isRunning()
            }

            2 -> {
                stopWatch.pause()
                isStopWatchRunning.value = stopWatch.isRunning()
            }

            3 -> {
                stopWatch.start()
                isStopWatchRunning.value = stopWatch.isRunning()
            }

            4 -> {
                stopWatch.pause()
                groupTraining = groupTraining.copy(trainingDuration = stopWatch.getTimeMillis())
                groupTraining = groupTraining.copy(trainingState = 4)
                onEndGroupTrainingClick(groupTraining)
            }
        }
    }

    qrBitmap?.let { bitmap ->

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Heading1("Skupinovy trening")
                    },
                    actions = {
                        Row {
                            if (!fullscreenQrState.value) {
                                IconButton(onClick = { fullscreenQrState.value = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.QrCode,
                                        contentDescription = "Show qr code icon"
                                    )
                                }
                            }

                            if (currentUser.id == chosenGroupTraining.trainerId) {
                                IconButton(onClick = { onDeleteClick() }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete training",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                )
            },
            bottomBar = {

                if (currentUser.id == chosenGroupTraining.trainerId) {
                    Row() {
                        if (stopWatch.getTimeMillis() == 0L && !isStopWatchRunning.value) {
                            CustomButton(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .weight(1f),
                                onClick = {
//                                    stopWatch.start()
//                                    isStopWatchRunning.value = stopWatch.isRunning()
                                    setTrainingState(1)
                                    //onSendAllUsers(groupTraining.trainingIndex)
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                buttonText = "Sputstit"
                            )
                        } else {

                            if (isStopWatchRunning.value) {
                                CustomButton(
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .weight(1f),
                                    onClick = {
//                                        stopWatch.pause()
//                                        isStopWatchRunning.value = stopWatch.isRunning()
                                        setTrainingState(2)
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
//                                        stopWatch.start()
//                                        isStopWatchRunning.value = stopWatch.isRunning()
                                        setTrainingState(3)
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
                                    //stopWatch.pause()
                                    setTrainingState(4)
//                                    groupTraining =
//                                        groupTraining.copy(trainingDuration = stopWatch.getTimeMillis())
                                    //onEndGroupTrainingClick(groupTraining)

                                },
                                containerColor = MaterialTheme.colorScheme.error,
                                buttonText = "Dokoncit"
                            )
                        }
                    }
                }
            }

        ) { innerPadding ->


            val padding: Dp = 8.dp
            Surface(modifier = Modifier.padding(innerPadding)) {

                if (fullscreenQrState.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                        //.background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.7f))
                        ,
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Column(Modifier.fillMaxWidth()) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                                IconButton(
                                    onClick = {
                                        fullscreenQrState.value = false
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        "Close qr code fullscreen icon",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "QR Code",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp),
                    contentAlignment = Alignment.TopCenter
                ) {

                    Column {

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                            if (chosenGroupTraining.name.isNotEmpty()) {
                                Heading2(
                                    "(${chosenGroupTraining.name})",
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(Modifier.padding(3.dp))
                            }
                            Heading2(trainingList[chosenGroupTraining.trainingIndex].name)
                            Spacer(Modifier.padding(3.dp))
                            Heading2(stopWatch.getCustomFormattedTime())
                        }

                        LazyColumn {

                            items(allTrainingParticipants) { participant ->

                                ListItem(
                                    headlineContent = { NormalText(participant.displayName) },
                                    overlineContent = { NormalText(participant.bio) },
                                    //supportingContent = {},
                                    leadingContent = {
                                        CustomProfilePictureFrame(
                                            pictureUrl = participant.profilePicUrl,
                                            frameColor = androidx.compose.ui.graphics.Color(
                                                frameColors[participant.color]
                                            )
                                        )
                                    },
                                    //trailingContent = {},
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun GroupTrainingLobbyPreview() {
    GroupTrainingLobby(
        chosenGroupTraining = GroupTraining(
            trainerId = "CnHNxTvP9UgT6Vbj8Xh8XFYcPmg1",
            name = "Ranny beh",
            trainingIndex = 0,
            maxUsers = 4,
            trainingDuration = 0,
            timeDateOfTraining = 1732623634,
            id = "04zGShmrMuJ0OekUfoR4",
            numberOfParticipants = 1,
            trainingDetails = "Toto su detaily treningu"
        ),
        onDeleteClick = {},
        currentUser = User(
            id = "",
            displayName = "Pavol P",
            profilePicUrl = "https://lh3.googleusercontent.com/a/ACg8ocK99KLif73J19brC6OLouuXT7I3ilQ-ArQApkXRHkuzZGOruVqf=s96-c",
            birthDate = 0,
            height = 0F,
            weight = 0F,
            bio = "Nie",
            color = 0,
            isTrainer = false
        ),
        onEndGroupTrainingClick = { },
        allTrainingParticipants = emptyList(),
        //onCheckAllTrainingInfo = { return@GroupTrainingLobby false },
        setTrainingState = {},
        onSendAllUsers = {}
    )
}