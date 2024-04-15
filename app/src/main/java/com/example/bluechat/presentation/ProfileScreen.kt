package com.example.bluechat.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import  androidx.compose.material3.Button
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.setValue
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ProfileScreen(
    profileData: () -> String,
    onSubmitProfileData: (String) -> Unit
) {
    ProfileScreenAppBarr(
        profileData = profileData,
        onSubmitProfileData = onSubmitProfileData
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenAppBarr(
    profileData: () -> String,
    onSubmitProfileData: (String) -> Unit
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
                }
            )
        }, content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                UserProfileEdit(
                    profileData = profileData,
                    onSubmitProfileData = onSubmitProfileData
                )
            }
        }
    )
}

@Composable
fun UserProfileEdit(
    profileData: () -> String,
    onSubmitProfileData: (String) -> Unit
) {
    val message = rememberSaveable {
        mutableStateOf(profileData())
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile picture section
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape) // Make the box round
                .clickable {
                    /* Handle profile picture upload click here */
                }
                .border(
                    BorderStroke(width = 1.dp, color = Color(0xFF96B3ED)),
                    shape = CircleShape
                )
        ) {

            // Add an icon or placeholder text inside the box
//                    Icon(
//                        Icons.Filled.Person
//                        contentDescription = "Profile Picture",
//                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
//                    ) // Adjust alpha for semi-transparent icon
        }
        Spacer(modifier = Modifier.height(16.dp)) // Add some spacing

        // Username text area
        OutlinedTextField(
            value = message.value, // Initial text
            onValueChange = { message.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Username") },
            maxLines = 1 // Set single line for username
        )
        Button(
            modifier = Modifier.padding(top = 10.dp),
            onClick = {
                onSubmitProfileData(message.value)
                message.value = message.value
                keyboardController?.hide()
            }
        ) {
            Text("Submit")
        }

    }
}
