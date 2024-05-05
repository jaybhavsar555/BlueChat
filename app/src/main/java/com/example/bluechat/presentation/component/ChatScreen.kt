@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.bluechat.presentation.component

import android.provider.ContactsContract.Profile
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bluechat.domain.chat.BluetoothChat
import com.example.bluechat.domain.chat.BluetoothMessage
import com.example.bluechat.presentation.BluetoothUiState
import com.example.bluechat.presentation.DevicesList
import com.example.bluechat.presentation.ProfileScreen
import com.example.bluechat.utils.prefs.SharedPreferencesManager
import com.google.gson.Gson

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ChatScreen(
    state: BluetoothUiState,
    onDisconnect: () -> Unit,
    onSendMessage: (String) -> Unit,
    onSingleBackupClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF96B3ED),
                    titleContentColor = Color(0xFF4D87F9),
                ),
                title = {
//                    androidx.compose.material3.Text(state.messages.last().senderName)
                    Text("BlueChat")
                },
                actions = {
                    IconButton(onClick = onDisconnect) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Disconnect"
                        )
                    }
                    androidx.compose.material3.IconButton(
                        onClick = onSingleBackupClick
                    ) {
                        androidx.compose.material3.Icon(
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
                ChatScreenWindow(
                    state = state,
                    onDisconnect = onDisconnect,
                    onSendMessage = onSendMessage
                )
            }
        }
    )
}

@Composable
fun ChatScreenWindow(
    state: BluetoothUiState,
    onDisconnect: () -> Unit,
    onSendMessage: (String) -> Unit,
) {
    val message = rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
////            Text(
////                text = "message",
////                modifier = Modifier.weight(1f)
////            )
////            IconButton(onClick = onDisconnect) {
////                Icon(
////                    imageVector = Icons.Default.Close,
////                    contentDescription = "Disconnect"
////                )
////            }
//        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.messages) { message ->
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ChatMessage(
                        message = message,
                        modifier = Modifier
                            .align(
                                if (message.isFromLocalUser) Alignment.End else Alignment.Start
                            )
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = message.value,
                onValueChange = { message.value = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(text = "Message")
                }
            )
            IconButton(onClick = {
                onSendMessage(message.value)
                message.value = ""
                keyboardController?.hide()
            }) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send message"
                )
            }
        }
    }
}