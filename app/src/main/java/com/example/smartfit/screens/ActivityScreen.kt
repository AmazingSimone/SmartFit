package com.example.smartfit.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartfit.components.NormalText
import com.example.smartfit.data.trainingList

@Composable
fun ActivityScreen() {

    Surface {

        LazyColumn {

            items(trainingList) { training ->

                ListItem(
                    modifier = Modifier.clickable {

                    },
                    headlineContent = { NormalText(training.name) },
                    leadingContent = {
                        Icon(
                            training.icon,
                            contentDescription = "Icon of a training"
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
    ActivityScreen()
}