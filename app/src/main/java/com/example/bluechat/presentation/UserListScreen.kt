package com.example.bluechat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


data class User(val profilePicture: Any, val username: String)
@ExperimentalMaterial3Api
class UserListScreen: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            UserList(users = listOf(
                User(profilePicture = Icons.Filled.Person, username = "Alice"),
                User(profilePicture = Icons.Filled.Person, username = "Bob"),
                User(profilePicture = Icons.Filled.Person, username = "Carol"),
                User(profilePicture = Icons.Filled.Person, username = "Carol"),
                User(profilePicture = Icons.Filled.Person, username = "Swaraj"),
                User(profilePicture = Icons.Filled.Person, username = "Vishawjit"),
                User(profilePicture = Icons.Filled.Person, username = "Mitali"),
                User(profilePicture = Icons.Filled.Person, username = "Jay"),
            ))
        }
    }

    @Composable
    fun UserList(users: List<User>) {
        Column(
            modifier = Modifier.fillMaxSize()
        )
        {
            for (user in users) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
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
                        Text(text = user.username, modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
}