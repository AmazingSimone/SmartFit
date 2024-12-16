package com.example.smartfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartfit.components.NormalText
import com.example.smartfit.data.trainingList

@Composable
fun ActivityScreen(
    onActivityClick: (Int) -> Unit
) {

    Surface(modifier = Modifier.fillMaxSize()) {

        LazyColumn(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLow)) {

            items(trainingList) { training ->

                ListItem(
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                    modifier = Modifier.clickable {

                        onActivityClick(trainingList.indexOf(training))

                    },
                    headlineContent = { NormalText(training.name) },
                    leadingContent = {
                        Icon(
                            training.icon,
                            contentDescription = "Icon of a training",
                            tint = MaterialTheme.colorScheme.surfaceTint
                        )
                    }
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview
@Composable
fun ActivityPreview() {
    ActivityScreen(
        {}
    )
}