package com.example.smartfit.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomIconButton
import com.example.smartfit.components.CustomOnlineStateIndicator
import com.example.smartfit.components.CustomProfilePictureFrame
import com.example.smartfit.components.HeadlineText

@Composable
fun NavigationUpAndBottomBar() {

    var selectedItem by remember { mutableIntStateOf(1) }
    val navBarItemsStrings = listOf("Domov", "Aktivita", "Priatelia")
    val navBarSelectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Favorite, Icons.Filled.Person)
    val navBarUnselectedIcons =
        listOf(Icons.Outlined.Home, Icons.Outlined.FavoriteBorder, Icons.Outlined.PersonOutline)

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Scaffold(
            topBar = {


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Box {
                        HeadlineText(navBarItemsStrings[selectedItem])
                    }

                    Box {
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (selectedItem == 0) {
                                CustomOnlineStateIndicator(onClick = {})
                                Spacer(Modifier.padding(horizontal = 5.dp))
                                CustomProfilePictureFrame()
                            } else if (selectedItem == 1) {
                                CustomIconButton(
                                    onClick = {},
                                    icon = Icons.Filled.History,
                                    size = 40.dp
                                )
                                //Icon(imageVector = Icons.Filled.History, contentDescription = "History icon")
                                Spacer(Modifier.padding(horizontal = 5.dp))
                                CustomIconButton(onClick = {}, icon = Icons.Filled.QrCodeScanner)
                            } else {
                                CustomIconButton(onClick = {}, icon = Icons.Filled.Search)
                            }

                        }
                    }
                }
            },

            bottomBar = {
                NavigationBar {

                    navBarItemsStrings.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    if (selectedItem == index) navBarSelectedIcons[index] else navBarUnselectedIcons[index],
                                    contentDescription = item
                                )
                            },
                            label = { if (selectedItem != index) Text(item) },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index }
                        )
                    }
                }
            }
        ) {

            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {



            }
        }
    }
}

@Preview
@Composable
fun PreviewNavbar() {
    NavigationUpAndBottomBar()
}