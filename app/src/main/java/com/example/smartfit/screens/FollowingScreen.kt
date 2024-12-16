package com.example.smartfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomProfilePictureFrame
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.NormalText
import com.example.smartfit.data.User
import com.example.smartfit.data.frameColors

@Composable
fun FollowingScreen(
    listOfUsers: List<User>,
    onUserClick: (String) -> Unit
) {

    Surface {


        if (listOfUsers.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                LazyColumn {

                    items(listOfUsers) {

                            user ->

                        ListItem(
                            modifier = Modifier.clickable {

                                onUserClick(user.id)

                            },
                            headlineContent = { NormalText(user.displayName) },
                            supportingContent = { if (user.bio.isNotEmpty()) NormalText(user.bio) },
                            leadingContent = {
                                CustomProfilePictureFrame(
                                    pictureUrl = user.profilePicUrl,
                                    frameColor = Color(frameColors[user.color])
                                )
                            },
                            colors = ListItemDefaults.colors(
                                containerColor =
                                if (user.color != 0)
                                    Color(frameColors[user.color]).copy(
                                        alpha = 0.07f
                                    )
                                else
                                    MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        )
                        HorizontalDivider()

                    }

                }
            }


        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Heading1("Klikni na ikonu lupy a vyhladaj dalsich ludi")
            }
        }

    }

}

@Preview
@Composable
fun FollowingPreview() {
    FollowingScreen(
        listOfUsers = emptyList(),
        onUserClick = { }
    )
}