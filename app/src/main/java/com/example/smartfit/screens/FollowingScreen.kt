package com.example.smartfit.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartfit.components.CustomProfilePictureFrame
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.NormalText
import com.example.smartfit.data.User
import com.example.smartfit.data.frameColors

@Composable
fun FollowingScreen(
    listOfUsers: List<User>,
    onUserClick: (Int) -> Unit
) {

    Surface {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            if (listOfUsers.isNotEmpty()) {

                LazyColumn {

                    items(listOfUsers) {

                            user ->

                        ListItem(
                            modifier = Modifier.clickable {

                                onUserClick(listOfUsers.indexOf(user))

                            },
                            headlineContent = { NormalText(user.displayName) },
                            supportingContent = { NormalText(user.bio) },
                            leadingContent = {

                                CustomProfilePictureFrame(
                                    pictureUrl = user.profilePicUrl,
                                    frameColor = Color(frameColors[user.color])
                                )

                            }
                        )
                        HorizontalDivider()

                    }

                }

            } else {
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