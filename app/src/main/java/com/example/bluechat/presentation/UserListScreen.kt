package com.example.bluechat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bluechat.domain.chat.BluetoothDevice


data class User(val profilePicture: Any, val username: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    state: BluetoothUiState,
    onStartChatClick: () -> Unit,
    onListenChatClick: (BluetoothDevice) -> Unit,
    onProfileClick: () -> Unit,
    onGotoAllDevicesClick: () -> Unit,
    onGeneralBackupClick: () -> Unit,
    profileData: (BluetoothDevice) -> String
) {
    UserListAppBarr(
        state = state,
        onStartChatClick = onStartChatClick,
        onListenChatClick = onListenChatClick,
        onProfileClick = onProfileClick,
        onGotoAllDevicesClick = onGotoAllDevicesClick,
        onGeneralBackupClick = onGeneralBackupClick,
        profileData = profileData
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListAppBarr(
    state: BluetoothUiState,
    onStartChatClick: () -> Unit,
    onListenChatClick: (BluetoothDevice) -> Unit,
    onProfileClick: () -> Unit,
    onGotoAllDevicesClick: () -> Unit,
    onGeneralBackupClick: () -> Unit,
    profileData: (BluetoothDevice) -> String
) {
    Scaffold(
        topBar = {

            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF96B3ED),
                    titleContentColor = Color(0xFF4D87F9),
                ),
                title = {
                    Text("BlueChat")
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "more options",
                        )
                    }
                }
            )
        }, content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                UserList(
                    state = state,
                    onStartClick = onStartChatClick,
                    onListenClick = onListenChatClick,
                    profileData = profileData
//        listOf(
//            User(profilePicture = Icons.Filled.Person, username = "Alice"),
//            User(profilePicture = Icons.Filled.Person, username = "Bob"),
//            User(profilePicture = Icons.Filled.Person, username = "Carol"),
//            User(profilePicture = Icons.Filled.Person, username = "Carol"),
//            User(profilePicture = Icons.Filled.Person, username = "Swaraj"),
//            User(profilePicture = Icons.Filled.Person, username = "Vishawjit"),
//            User(profilePicture = Icons.Filled.Person, username = "Mitali"),
//            User(profilePicture = Icons.Filled.Person, username = "Jay"),
//        )
                )
            }
        }
    )
}


@Composable
fun UserList(
    state: BluetoothUiState,
    onStartClick: () -> Unit,
    onListenClick: (BluetoothDevice) -> Unit,
    profileData: (BluetoothDevice) -> String
) {
    Column(
        modifier = Modifier.fillMaxSize()
    )
    {
        for (user in state.chatListDevices) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape) // Make the box round
                            .clickable {
                                /* Handle profile picture upload click here */
                            }
                            .border(
                                BorderStroke(width = 1.dp, color = Color(0xFF96B3ED)),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Text(text = profileData(user) ?: "User", modifier = Modifier.padding(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Start",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { onStartClick() })
                        Text(
                            text = "Listen",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { onListenClick(user) })
                    }
                }
            }
        }
    }
}
