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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
//import androidx.compose.material3.icons.Icons.Filled.Person
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
@ExperimentalMaterial3Api
class ProfileScreen: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppBarr()
//            AppbarWSwitch()
            UserProfileEdit()

        }
    }
    @Composable
    fun AppBarr(){
        Scaffold(
            topBar = {

                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF96B3ED),
                        titleContentColor = Color(0xFF4D87F9),
                    ),
                    title ={
                        Text("BlueChat")
                    }
                )
            },
        ) {
        }
    }
    @Composable
    fun BluetoothSwitchButton(){
        val mCheckedState = remember{ mutableStateOf(false)}

        Switch(
            checked = mCheckedState.value,
            onCheckedChange = {
                mCheckedState.value = it
                if(it){
//
                }
                else{
                    null
                }

            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF96B3ED),
                uncheckedThumbColor = Color(0xFF96B3ED),
                checkedTrackColor = Color(0xFFFFFFFF),
                uncheckedTrackColor = Color(0xFFFFFFFF)

            ),
        )
    }

    @Composable
    fun AppbarWSwitch(){
        Scaffold(
            topBar = {

                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF96B3ED),
                        titleContentColor = Color(0xFF4D87F9),
                    ),
                    title ={
                        Text("BlueChat")
                    },
                    actions = {
//                        BluetoothSwitchButton()

                    }
                )
            },
        ){
        }

    }

    @Composable
    fun UserProfileEdit() {
        Scaffold {
            Surface() {
                Column(
                    modifier = Modifier.fillMaxSize().padding(30.dp),
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
                        value = "Enter your username", // Initial text
                        onValueChange = { /* Update username state here */ },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Username") },
                        maxLines = 1 // Set single line for username
                    )
                    Button(

                        onClick = {
                            //handle the Updated Image and Device Name

                            /**after successfully Updated it will Show The Toast and navigate to the
                             * ConnectToService Screen
                             */
                            val vintent =
                                Intent(this@ProfileScreen, ConnectToDeviceScreen::class.java);
                            startActivity(vintent);
                        }
                    ) {
                        Text("Submit")
                    }

                }
            }
        }
    }

}