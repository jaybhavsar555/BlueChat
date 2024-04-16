package com.example.bluechat.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bluechat.domain.chat.BluetoothDevice
import com.example.bluechat.utils.theme.BlueChatTheme

@Composable
fun AllDeviceScreen(
    state: BluetoothUiState,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    onStartServer: () -> Unit,
    onScannedDeviceClick: (BluetoothDevice) -> Unit,
    onPairedDeviceClick: (BluetoothDevice) -> Unit,
    onStartChat: () -> Unit
) {
    BlueChatTheme {
        val customBlue = Color(0xFF4D87F9)
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            BluetoothDeviceList(
                pairedDevices = state.pairedDevices,
                scannedDevices = state.scannedDevices,
                onScanDeviceClick = onScannedDeviceClick,
                onPairDeviceClick = onPairedDeviceClick,
                onStartChat = onStartChat,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = onStartScan,
//                modifier = Modifier.fillMaxWidth(0.5f),
                    colors = ButtonDefaults.buttonColors(customBlue)
                ) {
                    Text(
                        text = "Start scan",
                        color = Color.White
                    )
                }
//            Button(onClick = onStopScan) {
//                Text(text = "Stop scan")
//            }
                Button(
                    onClick = onStartServer,
//                modifier = Modifier.fillMaxWidth(0.5f),
                    colors = ButtonDefaults.buttonColors(customBlue)
                ) {
                    Text(
                        text = "Start Pair",
                        color = Color.White
                    )
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothDeviceList(
    pairedDevices: List<BluetoothDevice>,
    scannedDevices: List<BluetoothDevice>,
    onScanDeviceClick: (BluetoothDevice) -> Unit,
    onPairDeviceClick: (BluetoothDevice) -> Unit,
    onStartChat: () -> Unit,
    modifier: Modifier = Modifier
) {


    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF96B3ED),
            titleContentColor = Color(0xFF4D87F9),
        ),
        title = {
            androidx.compose.material3.Text(
                "BlueChat",
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = onStartChat) {
                Icon(
                    imageVector = Icons.Filled.MailOutline,
                    contentDescription = "start chat",
                )
            }
        }
    )

    LazyColumn(
        modifier = modifier
    ) {
        item {
            Text(
                text = "Paired Devices",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(pairedDevices) { device ->
            Text(
                text = device.name ?: device.address!!,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPairDeviceClick(device) }
                    .padding(16.dp)
            )
        }

        item {
            Text(
                text = "Scanned Devices",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(scannedDevices) { device ->
            Text(
                text = device.name ?: device.address!!,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onScanDeviceClick(device) }
                    .padding(16.dp)
            )
        }
    }
}