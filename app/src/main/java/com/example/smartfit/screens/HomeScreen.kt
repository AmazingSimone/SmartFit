package com.example.smartfit.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.R
import com.example.smartfit.components.CustomInfoCardFromDevice

@Composable
fun HomeScreen() {

    Surface {

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            CustomInfoCardFromDevice("Tep", 0, unit = "t/m", image = R.drawable.heart)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice("Teplota", 0, unit = "°C", image = R.drawable.temperature)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice("Kroky", 0, unit = "", image = R.drawable.step)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice("Kadencia", 0, unit = "", image = R.drawable.runn)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice("Spalene kalorie", 0, unit = "", image = R.drawable.fire)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice("Vzdialenost", 0, unit = "m", image = R.drawable.steps)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice("Saturacia", 0, unit = "%", image = R.drawable.blood)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice("Rychlost", 0, unit = "km/h", image = R.drawable.speed)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            CustomInfoCardFromDevice("Vo2 max", 0, unit = "", image = R.drawable.vo2)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))

        }
    }

}

@Preview
@Composable
fun HomePreview() {

    HomeScreen()

}