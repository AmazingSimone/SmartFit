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

@Composable
fun HomeScreen(
    nrfData: NrfData
) {

    Surface {

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            CustomInfoCardFromDevice("Tep", nrfData.tep, unit = "t/m", image = R.drawable.heart)
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

@Preview
@Composable
fun HomePreview() {

    HomeScreen(NrfData())

}