package com.example.bluechat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bluechat.R
import com.example.bluechat.domain.chat.BluetoothDevice
import kotlinx.coroutines.launch


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
    val expanded = remember { mutableStateOf(false) }

    fun toggleMenu() {
        expanded.value = !expanded.value
    }
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
                    IconButton(onClick = { toggleMenu() }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "more options",
                        )
                        DropDownOptions(
                            expanded = expanded,
                            toggleMenu = { toggleMenu() },
                            onProfileClick = onProfileClick,
                            onGotoAllDevicesClick = onGotoAllDevicesClick,
                            onGeneralBackupClick = onGeneralBackupClick
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
fun DropDownOptions(
    expanded: MutableState<Boolean>,
    toggleMenu: () -> Unit,
    onProfileClick: () -> Unit,
    onGotoAllDevicesClick: () -> Unit,
    onGeneralBackupClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Content of the screen goes here

        // Dropdown menu
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = toggleMenu,
        ) {
            DropdownMenuItem(onClick =
            onProfileClick
//                toggleMenu
                ,
                text = {
                    Text(text = "Profile")
                })
            DropdownMenuItem(onClick =
            onGotoAllDevicesClick
//                toggleMenu
                ,
                text = {
                    Text(text = "Go to Devices")
                })
            DropdownMenuItem(onClick =
            onGeneralBackupClick,
                text = {
                    Text(text = "Backup")
                })
            // Add more menu items as needed
        }
    }
}

@Composable
fun UserList(
    state: BluetoothUiState,
    onStartClick: () -> Unit,
    onListenClick: (BluetoothDevice) -> Unit,
    profileData: (BluetoothDevice) -> String,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    )
    {
        for (user in state.chatListDevices) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(4.dp),

                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                )

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),

                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    //profile pic
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .fillMaxHeight()
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
                    //username
                    Text(
                        text = profileData(user) ?: "User",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium

                    )
                    //start and Listen Btn
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End

                    ) {

                        //added an image icon to start an listen connection
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Start",
                            modifier = Modifier
                                .clickable {
                                    onStartClick()
                                }
                                .padding(8.dp)
                        )

                        Icon(
                            modifier = Modifier
                                .clickable {
                                    onListenClick(user)
                                }
                                .padding(10.dp),
                            painter = painterResource(id = R.drawable.baseline_hearing_24),
                            contentDescription = "Listen",

                            )


                    }
                }
            }
        }
    }
}
