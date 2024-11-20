package com.example.smartfit.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.smartfit.components.HeadlineText

@Composable
fun FollowingScreen() {

    Surface {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            HeadlineText("Friends Screen")

        }

    }

}