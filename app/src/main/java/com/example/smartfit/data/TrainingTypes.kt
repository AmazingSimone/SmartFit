package com.example.smartfit.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.ui.graphics.vector.ImageVector


data class Training(
    val name: String,
    val icon: ImageVector,
    //val creatorId: String = "",
    val trainingDuration: Long = 0L,
    val timeDateOfTraining: Long = 0L,
    val avgSpeed: Float = 0F,
    val burnedCalories: Float = 0F,
    val avgHeartRate: Int = 0,
    val avgTempo: Int = 0,
    val steps: Int = 0,
    val trainingTemperature: Int = 0
)


//TODO Kolekcia Group treningy bude mat na firestore v sebe kolekciu users kt. bude tvorit cisto userIds
//TODO potreba potom spravit metodu ktora mi getne trening podla userId a treningId
//TODO a tiez treba spravit createGroupTraining ktora vytvori skup. trening a vrati jeho id
data class GroupTraining(
    val trainerId: String,
    val name: String = "",
    val trainingIndex: Int,
    val maxUsers: Int,
    val trainingDuration: Long = 0L,
    val timeDateOfTraining: Long = 0L
)


val trainingList = listOf (
    Training("Beh", Icons.AutoMirrored.Filled.DirectionsRun),
    Training("Bicyklovanie", Icons.AutoMirrored.Filled.DirectionsRun),
    Training("Bezecky trener", Icons.AutoMirrored.Filled.DirectionsRun),
    Training("Beh na drahe", Icons.AutoMirrored.Filled.DirectionsRun),
    Training("Bezecky pas", Icons.AutoMirrored.Filled.DirectionsRun),
    Training("Bicyklovanie v nutri", Icons.AutoMirrored.Filled.DirectionsRun),
    Training("Elipticky trenazer", Icons.AutoMirrored.Filled.DirectionsRun),
    Training("Skakanie cez svihadlo", Icons.AutoMirrored.Filled.DirectionsRun),
    Training("Stepper", Icons.AutoMirrored.Filled.DirectionsRun),
    Training("Stupanie po schodoch", Icons.AutoMirrored.Filled.DirectionsRun),
    Training("Turistika", Icons.AutoMirrored.Filled.DirectionsRun)
)