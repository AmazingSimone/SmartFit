package com.example.smartfit.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomInfoCardFromDevice

@Composable
fun HomeScreen() {

    Surface {

        Column(
            modifier = Modifier.padding(horizontal = 8.dp).verticalScroll(rememberScrollState())
        ) {
            CustomInfoCardFromDevice("Heading1", 0, unit = " --", icon = Icons.Filled.ErrorOutline)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice("Heading1", 0, unit = " --", icon = Icons.Filled.ErrorOutline)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice("Heading1", 0, unit = " --", icon = Icons.Filled.ErrorOutline)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice("Heading1", 0, unit = " --", icon = Icons.Filled.ErrorOutline)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice("Heading1", 0, unit = " --", icon = Icons.Filled.ErrorOutline)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice("Heading1", 0, unit = " --", icon = Icons.Filled.ErrorOutline)
        }
    }

}

@Preview
@Composable
fun HomePreview() {

    HomeScreen()

}