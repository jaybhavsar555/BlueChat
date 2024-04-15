package com.example.bluechat

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bluechat.domain.chat.BluetoothDevice
import com.example.bluechat.presentation.AllDeviceScreen
import com.example.bluechat.presentation.BluetoothOnOffScreen
import com.example.bluechat.presentation.BluetoothUiState
import com.example.bluechat.presentation.BluetoothViewModel
import com.example.bluechat.presentation.ProfileScreen
import com.example.bluechat.presentation.UserListScreen
import com.example.bluechat.presentation.component.ChatScreen
import com.example.bluechat.presentation.component.DeviceScreen
import com.example.bluechat.utils.prefs.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

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

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

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
                            onSendMessage = viewModel::sendMessage,
                            onSingleBackupClick = {}
                        )
                    }

                    else -> {
                        Navigation(
                            state = state,
                            startScan = viewModel::startScan,
                            stopScan = viewModel::stopScan,
                            startServer = viewModel::waitForIncomingConnections,
                            connectDevice = viewModel::connectToDevice,
                            senderProfileData = viewModel::getSavedSenderProfileDataFromPrefs,
                            userProfileData = viewModel::getSavedUserProfileDataFromPrefs,
                            onSubmitProfileData = viewModel::saveProfileData,
                            addDeviceToChatList = viewModel::addDeviceToChatList
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun Navigation(
        state: BluetoothUiState,
        startScan: () -> Unit,
        stopScan: () -> Unit,
        startServer: () -> Unit,
        connectDevice: (BluetoothDevice) -> Unit,
        senderProfileData: (BluetoothDevice) -> String,
        userProfileData: () -> String,
        onSubmitProfileData: (String) -> Unit,
        addDeviceToChatList: (BluetoothDevice) -> Unit
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "on_off_screen") {
            composable(route = "all_device_screen") {
                AllDeviceScreen(
                    state = state,
                    onStartScan = startScan,
                    onStopScan = stopScan,
                    onScannedDeviceClick = connectDevice,
                    onPairedDeviceClick = addDeviceToChatList,
                    onStartServer = startServer,
                    onStartChat = { navController.navigate("user_list_screen") }
                )
            }
            composable(route = "user_list_screen") {
                UserListScreen(
                    state = state,
                    onStartChatClick = startServer,
                    onListenChatClick = connectDevice,
                    onProfileClick = { navController.navigate("profile_screen") },
                    onGotoAllDevicesClick = { navController.navigate("all_device_screen") },
                    onGeneralBackupClick = {},
                    profileData = senderProfileData
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    launcher()
            }
            composable(route = "profile_screen") {
                ProfileScreen(
                    profileData = userProfileData,
                    onSubmitProfileData = onSubmitProfileData
                )
            }
            composable(route = "chat_screen") {

            }
            composable(route = "on_off_screen") {
                if (sharedPreferencesManager.getBoolean(SharedPreferencesManager.NO_FRESH_INSTALL)) {
                    if (!isBluetoothEnabled || !hasPermission(Manifest.permission.BLUETOOTH_CONNECT) ||
                        !hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
                        launcher()
                    }
                    navController.navigate("user_list_screen")
                } else {
                    BluetoothOnOffScreen(onOnOffClick = {
                        if (it) {
                            launcher()
                            navController.navigate("all_device_screen")
                            sharedPreferencesManager.saveBoolean(
                                SharedPreferencesManager.NO_FRESH_INSTALL,
                                true
                            )
                        }
                    })
                }
            }
        }
    }


    private fun launcher() {
        try {
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
        } catch (e: Exception) {
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
//        if (!isBluetoothEnabled)
//            launcher()
    }
}


