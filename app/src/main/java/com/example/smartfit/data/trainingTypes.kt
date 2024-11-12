package com.example.smartfit.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.ui.graphics.vector.ImageVector

data class Training(
    val name: String,
    val icon: ImageVector
)

val trainings = listOf (
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