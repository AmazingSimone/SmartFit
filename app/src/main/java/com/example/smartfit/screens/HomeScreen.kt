package com.example.smartfit.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.R
import com.example.smartfit.components.CustomDailyActivityCard
import com.example.smartfit.components.CustomInfoCardFromDevice
import com.example.smartfit.data.NrfData
import com.example.smartfit.data.Training
import com.example.smartfit.data.User
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun HomeScreen(
    nrfData: NrfData,
    user: User,
    listOfTrainings: List<Training>
) {

    Surface {

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            CustomDailyActivityCard(
                heading = "Denna aktivita",
                activity = getTodayTrainingsTotalMinutesDuration(listOfTrainings).toString(),
                activityGoal = user.activityGoal.ifEmpty { "90" },
                steps = getTodayTrainingsTotalSteps(listOfTrainings).toString(),
                stepsGoal = user.stepsGoal.ifEmpty { "10000" },
                calories = getTodayTrainingsTotalCalories(listOfTrainings).toString(),
                caloriesGoal = user.caloriesGoal.ifEmpty { "500" }
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            CustomInfoCardFromDevice(
                "Tep", nrfData.tep, unit = "t/m", image = R.drawable.heart,
                color = Color(0xFFB71C1C)
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice(
                "Teplota",
                nrfData.teplota,
                unit = "°C",
                image = R.drawable.temperature,
                color = Color(0xFF6200EE)
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice(
                "Kroky", nrfData.kroky, unit = "", image = R.drawable.step,
                color = Color(0xFF4CAF50)
            )
//            Spacer(modifier = Modifier.padding(vertical = 8.dp))
//            CustomInfoCardFromDevice(
//                "Kadencia",
//                nrfData.kadencia,
//                unit = "",
//                image = R.drawable.runn
//            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice(
                "Spalene kalorie",
                nrfData.spaleneKalorie,
                unit = "kcal",
                image = R.drawable.fire,
                color = Color(0xFFFFC107)
            )
            //TODO treba dorobit aby sa to zmenilo neskor na kilometre to plati aj pre prebiehajuci trening
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice(
                "Vzdialenost",
                nrfData.vzdialenost,
                unit = "m",
                image = R.drawable.steps,
                color = Color(0xFF388E3C)
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice(
                "Saturacia",
                nrfData.saturacia,
                unit = "%",
                image = R.drawable.blood,
                color = Color(0xFF0D47A1)
            )
//            Spacer(modifier = Modifier.padding(vertical = 8.dp))
//            CustomInfoCardFromDevice(
//                "Rychlost",
//                nrfData.rychlost,
//                unit = "km/h",
//                image = R.drawable.speed
//            )
//            Spacer(modifier = Modifier.padding(vertical = 8.dp))
//            CustomInfoCardFromDevice("Vo2 max", nrfData.vo2Max, unit = "", image = R.drawable.vo2)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))

        }
    }

}

private fun getTodayTrainingsTotalMinutesDuration(listOfTrainings: List<Training>): Long {
    if (listOfTrainings.isEmpty()) return 0

    val today = LocalDate.now()
    val startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
    val endOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond()

    val todayTrainings = listOfTrainings.filter { training ->
        training.timeDateOfTraining in startOfDay until endOfDay
    }.map { training ->
        if (training.steps == 0 && training.burnedCalories == 0) {
            training.copy(trainingDuration = 0L)
        } else {
            training
        }
    }

    val totalDurationMillis = todayTrainings.sumOf { it.trainingDuration }
    return totalDurationMillis / 60000
}

private fun getTodayTrainingsTotalCalories(listOfTrainings: List<Training>): Long {
    if (listOfTrainings.isEmpty()) return 0

    val today = LocalDate.now()
    val startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
    val endOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond()

    val todayTrainings = listOfTrainings.filter { training ->
        training.timeDateOfTraining in startOfDay until endOfDay
    }

    return todayTrainings.sumOf { it.burnedCalories.toLong() }
}

private fun getTodayTrainingsTotalSteps(listOfTrainings: List<Training>): Long {
    if (listOfTrainings.isEmpty()) return 0

    val today = LocalDate.now()
    val startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
    val endOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond()

    val todayTrainings = listOfTrainings.filter { training ->
        training.timeDateOfTraining in startOfDay until endOfDay
    }

    return todayTrainings.sumOf { it.steps.toLong() }
}

@Preview
@Composable
fun HomePreview() {

    HomeScreen(
        NrfData(),
        user = User(),
        listOfTrainings = emptyList()
    )

}