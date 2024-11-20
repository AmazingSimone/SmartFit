package com.example.smartfit.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfit.components.CustomProfilePictureFrame
import com.example.smartfit.components.NormalText
import com.example.smartfit.data.User
import com.example.smartfit.data.frameColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchForUserScreen(
    onSearchQuery: (String) -> Unit,
    recievedUsersFromQuery: List<User>,
    onUserAddClick: () -> Unit,
    onUserProfileClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(true) }

    Box(
        Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
            .padding(top = 0.dp)) {
        val onActiveChange: (Boolean) -> Unit = { expanded = it }

        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = {
                        query = it
                        onSearchQuery(it)
                    },
                    onSearch = { },
                    expanded = true,
                    onExpandedChange = {},
                    placeholder = { NormalText("Vyhladaj pouzivatela") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search icon"
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (query.isNotEmpty()) {
                                    query = ""
                                    onSearchQuery("")
                                } else {
                                    onBackClick()
                                }
                            }
                        ) { Icon(Icons.Default.Close, contentDescription = "Close search icon") }
                    }
                )
            },
            expanded = true,
            onExpandedChange = onActiveChange,
            content = {

                LazyColumn {
                    items(recievedUsersFromQuery) { user ->

                        ListItem(
                            headlineContent = { Text(user.displayName) },
                            supportingContent = { Text(user.bio) },
                            leadingContent = {
                                CustomProfilePictureFrame(
                                    pictureUrl = user.profilePicUrl,
                                    frameColor = Color(frameColors[user.color])
                                )
                            },
                            trailingContent = {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = "Add user icon"
                                )
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            modifier = Modifier
                                .clickable {
                                    onUserProfileClick(user.id)
                                }
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            },
        )
    }
}

@Preview
@Composable
fun SearchForUserPreview() {
    SearchForUserScreen(
        onUserAddClick = {},
        onUserProfileClick = {},
        recievedUsersFromQuery = emptyList(),
        onSearchQuery = {},
        onBackClick = {}
    )
}