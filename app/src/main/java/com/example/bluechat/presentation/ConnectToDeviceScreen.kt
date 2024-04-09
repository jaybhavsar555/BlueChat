package com.example.bluechat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ConnectToDeviceScreen() {
    ConnectToDeviceScreenAppBarr()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectToDeviceScreenAppBarr() {
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
                DevicesList()
            }
        }
    )
}

@Composable
fun DevicesList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ScannedDevices()
        PairedDevices()
    }
}

@Composable
fun ScannedDevices() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Hello, Jetpack Compose!")
    }
}

@Composable
fun PairedDevices() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Hello, Jetpack Compose!")
    }

}
