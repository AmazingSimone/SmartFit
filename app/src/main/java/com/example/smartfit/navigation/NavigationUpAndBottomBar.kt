package com.example.smartfit.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomIconButton
import com.example.smartfit.components.CustomOnlineStateIndicator
import com.example.smartfit.components.CustomProfilePictureFrame
import com.example.smartfit.components.HeadlineText
import com.example.smartfit.data.NrfData
import com.example.smartfit.data.User
import com.example.smartfit.data.frameColors
import com.example.smartfit.screens.ActivityScreen
import com.example.smartfit.screens.FollowingScreen
import com.example.smartfit.screens.HomeScreen

@Composable
fun NavigationUpAndBottomBar(
    onDeviceIndicatorClick: () -> Unit,
    onProfilePictureClick: (String) -> Unit,
    onActivityClick: (Int) -> Unit,
    onHistoryClick: () -> Unit,
    onFAButtonClick: (String) -> Unit,
    onQrCodeClick: () -> Unit,
    onUserClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    recievedUser: User,
    recievedListOfUsers: List<User>,
    isBLEConnected: Int,
    nrfData: NrfData
) {


    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val navBarItemsStrings = listOf("Domov", "Aktivita", "Sledujes")
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
                                CustomOnlineStateIndicator(
                                    onClick = { onDeviceIndicatorClick() },
                                    indicatorColor = when (isBLEConnected) {
                                        0 -> MaterialTheme.colorScheme.error
                                        1 -> Color.Yellow
                                        2 -> Color.Green
                                        else -> MaterialTheme.colorScheme.error
                                    }
                                )
                                Spacer(Modifier.padding(horizontal = 5.dp))
                                CustomProfilePictureFrame(
                                    pictureUrl = recievedUser.profilePicUrl.toString(),
                                    frameColor = Color(frameColors[recievedUser.color]),
                                    onClick = { onProfilePictureClick(recievedUser.id) },
                                    enabled = true
                                )
                            } else if (selectedItem == 1) {
                                CustomIconButton(
                                    onClick = { onHistoryClick() },
                                    icon = Icons.Filled.History,
                                    size = 40.dp
                                )
                                //Icon(imageVector = Icons.Filled.History, contentDescription = "History icon")
                                Spacer(Modifier.padding(horizontal = 5.dp))
                                CustomIconButton(
                                    onClick = { onQrCodeClick() },
                                    icon = Icons.Filled.QrCodeScanner
                                )
                            } else {
                                CustomIconButton(
                                    onClick = { onSearchClick() },
                                    icon = Icons.Filled.Search
                                )
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                if (selectedItem == 1 && recievedUser.isTrainer)
                    ExtendedFloatingActionButton(
                        onClick = { onFAButtonClick(recievedUser.id) },
                        icon = { Icon(Icons.Filled.Add, "Add group training icon") },
                        text = { Text(text = "Vytvorit skupinovy trening") },
                    )
            },

            bottomBar = {
                NavigationBar(
                    tonalElevation = 8.dp

                ) {

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
        ) { paddingValues ->

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
            ) {

                when (selectedItem) {
                    0 -> {
                        HomeScreen(nrfData)
                    }

                    1 -> {
                        ActivityScreen(onActivityClick)
                    }

                    else -> {
                        FollowingScreen(
                            listOfUsers = recievedListOfUsers,
                            onUserClick = { userId ->
                                onUserClick(userId)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewNavbar() {
    NavigationUpAndBottomBar(
        onDeviceIndicatorClick = {},
        onProfilePictureClick = {},
        onActivityClick = {},
        onHistoryClick = {},
        onQrCodeClick = {},
        onUserClick = {},
        onSearchClick = {},
        recievedUser = User(),
        recievedListOfUsers = emptyList(),
        onFAButtonClick = {},
        isBLEConnected = 0,
        nrfData = NrfData()
    )
}