package com.example.smartfit.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomTrainingInfoCardWithDate
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.Heading2
import com.example.smartfit.components.NormalText
import com.example.smartfit.data.Training
import com.example.smartfit.data.trainingList
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingHistoryScreen(
    listOfTrainings: List<Training>,
    onBackClick: () -> Unit,
    onActivityClick: (Int) -> Unit
) {

    val isLoading = rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(listOfTrainings) {
        if (listOfTrainings.isNotEmpty()) {
            isLoading.value = false
        } else {
            delay(1500)
            isLoading.value = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Heading1("Historia treningov") },
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
            if (isLoading.value) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.TopCenter
                ) {

                    if (listOfTrainings.isNotEmpty()) {
                        LazyColumn {
                            val groupedTrainings = listOfTrainings.groupBy {
                                LocalDate.ofEpochDay(it.timeDateOfTraining / (24 * 60 * 60))
                                    .with(DayOfWeek.MONDAY)
                            }

                            groupedTrainings.entries.withIndex().forEach { (index, entry) ->
                                val (week, trainingsThisWeek) = entry
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Heading2(
                                            if (week == LocalDate.now().with(DayOfWeek.MONDAY))
                                                "Tento tyzden"
                                            else
                                                "Tyzden od ${
                                                    week.format(
                                                        DateTimeFormatter.ofPattern(
                                                            "dd.MM.yyyy"
                                                        )
                                                    )
                                                }"
                                        )
                                        NormalText("Pocet aktivit ${trainingsThisWeek.size}")
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                                items(trainingsThisWeek.sortedByDescending { it.timeDateOfTraining }) { training ->
                                    CustomTrainingInfoCardWithDate(
                                        modifier = Modifier.clickable {
                                            onActivityClick(listOfTrainings.indexOf(training))
                                        },
                                        training = training
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                if (index < groupedTrainings.size - 1) {
                                    item {
                                        HorizontalDivider()
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }
                        }
                    } else {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Heading1("Ziadne minule treningy")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TrainingHistoryPreview() {
    TrainingHistoryScreen(
        listOfTrainings = trainingList,
        onBackClick = {},
        onActivityClick = {}
    )
}