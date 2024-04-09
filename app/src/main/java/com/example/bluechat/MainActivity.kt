package com.example.bluechat

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bluechat.presentation.AllDeviceScreen
import com.example.bluechat.presentation.BluetoothOnOffScreen
import com.example.bluechat.presentation.BluetoothViewModel
import com.example.bluechat.presentation.UserListScreen
import com.example.bluechat.presentation.component.ChatScreen
import com.example.bluechat.presentation.component.DeviceScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }
    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    var permissionLauncher: ActivityResultLauncher<Array<String>>? = null
    var enableBluetoothLauncher: ActivityResultLauncher<Intent>? = null
//    var enableBluetoothDiscoveryLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { /* Not needed */ }

//        enableBluetoothDiscoveryLauncher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { /* Not needed */ }

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                perms[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else true

//            if(canEnableBluetooth && !isBluetoothEnabled) {
//            enableBluetoothLauncher.launch(
//                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            )
//            }
//            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
//            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 500)
//            enableBluetoothDiscoveryLauncher?.launch(
//                intent
//            )
        }

        setContent {

            val viewModel = hiltViewModel<BluetoothViewModel>()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(key1 = state.errorMessage) {
                state.errorMessage?.let { message ->
                    Toast.makeText(
                        applicationContext,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            LaunchedEffect(key1 = state.isConnected) {
                if (state.isConnected) {
                    Toast.makeText(
                        applicationContext,
                        "You're connected!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            Surface(
                color = MaterialTheme.colors.background
            ) {
                when {
                    state.isConnecting -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                            Text(text = "Connecting...")
                        }
                    }

                    state.isConnected -> {
                        ChatScreen(
                            state = state,
                            onDisconnect = viewModel::disconnectFromDevice,
                            onSendMessage = viewModel::sendMessage
                        )
                    }

                    state.isOn -> {
                        launcher()
                        AllDeviceScreen(
                            state = state,
                            onStartScan = viewModel::startScan,
                            onStopScan = viewModel::stopScan,
                            onScannedDeviceClick = viewModel::connectToDevice,
                            onPairedDeviceClick = viewModel::addDeviceToChatList,
                            onStartServer = viewModel::waitForIncomingConnections
                        )

                    }

                    state.isDeviceAddedToChatList -> {
                        UserListScreen(
                            state = state,
                            onStartChatClick = viewModel::waitForIncomingConnections,
                            onListenChatClick = viewModel::connectToDevice
                        )
                    }

                    else -> {
                        if (!isBluetoothEnabled) {
                            BluetoothOnOffScreen(onOnOffClick = viewModel::handleOnOff)
                        } else {
                            AllDeviceScreen(
                                state = state,
                                onStartScan = viewModel::startScan,
                                onStopScan = viewModel::stopScan,
                                onScannedDeviceClick = viewModel::connectToDevice,
                                onPairedDeviceClick = viewModel::addDeviceToChatList,
                                onStartServer = viewModel::waitForIncomingConnections
                            )
                        }
                    }
                }
            }
        }
    }


    private fun launcher() {
        permissionLauncher?.launch(
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
        enableBluetoothLauncher?.launch(
            Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        )

//        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
//        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 500)
//        enableBluetoothDiscoveryLauncher?.launch(
//            intent
//        )
    }
}


