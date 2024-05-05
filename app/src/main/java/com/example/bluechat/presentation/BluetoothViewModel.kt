package com.example.bluechat.presentation

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bluechat.domain.chat.BluetoothChat
import com.example.bluechat.domain.chat.BluetoothChatToSenderAddress
import com.example.bluechat.domain.chat.BluetoothChatToUserAddress
import com.example.bluechat.domain.chat.BluetoothController
import com.example.bluechat.domain.chat.BluetoothDevice
import com.example.bluechat.domain.chat.BluetoothDeviceDomain
import com.example.bluechat.domain.chat.BluetoothDeviceList
import com.example.bluechat.domain.chat.BluetoothMessage
import com.example.bluechat.domain.chat.ConnectionResult
import com.example.bluechat.utils.network.NetworkHelper
import com.example.bluechat.utils.prefs.SharedPreferencesManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bluetoothController: BluetoothController,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val chatListDevices: List<String> = emptyList()


    private val _state = MutableStateFlow(BluetoothUiState())
    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        _state,
    ) { scannedDevices, pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
            messages = if (state.isConnected) state.messages else emptyList()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    private var deviceConnectionJob: Job? = null

    init {
        bluetoothController.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)

        bluetoothController.errors.onEach { error ->
            _state.update {
                it.copy(
                    errorMessage = error
                )
            }
        }.launchIn(viewModelScope)

        val savedJsonData =
            sharedPreferencesManager.getString(SharedPreferencesManager.SAVED_DEVICES)
        val savedDevicesPrefs =
            Gson().fromJson(savedJsonData, BluetoothDeviceList::class.java)
        savedDevicesPrefs?.let {
            _state.update { it.copy(chatListDevices = savedDevicesPrefs.listDevices) }
        }
    }

    fun connectToDevice(device: BluetoothDeviceDomain) {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController
            .connectToDevice(device)
            .listen()
    }

    fun addDeviceToChatList(newDevice: BluetoothDevice) {
        _state.update {
            if (newDevice in it.chatListDevices) it.copy(
                chatListDevices = it.chatListDevices
            ) else it.copy(
                chatListDevices = it.chatListDevices.plusElement(
                    newDevice
                )
            )
        }
        val gson = Gson()
        val bluetoothDeviceList = BluetoothDeviceList(state.value.chatListDevices)
        val jsonData = gson.toJson(bluetoothDeviceList)
        sharedPreferencesManager.saveString(SharedPreferencesManager.SAVED_DEVICES, jsonData)
    }

    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothController.closeConnection()
        _state.update {
            it.copy(
                isConnecting = false,
                isConnected = false
            )
        }
    }

    fun waitForIncomingConnections() {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController
            .startBluetoothServer()
            .listen()
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val bluetoothMessage = bluetoothController.trySendMessage(message)
            if (bluetoothMessage != null) {
                _state.update {
                    it.copy(
                        messages = it.messages + bluetoothMessage
                    )
                }
                val gson = Gson()
                val bluetoothChat = BluetoothChat(_state.value.messages)
                val jsonData = gson.toJson(bluetoothChat)
                sharedPreferencesManager.saveString(
                    "${SharedPreferencesManager.SAVED_CHATS}_${
                        sharedPreferencesManager.getString(
                            SharedPreferencesManager.SENDER_ADDRESS
                        )
                    }",
                    jsonData
                )
            }
        }
    }

    fun startScan() {
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        bluetoothController.stopDiscovery()
    }

    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when (result) {
                ConnectionResult.ConnectionEstablished -> {
                    val savedChats =
                        sharedPreferencesManager.getString("${SharedPreferencesManager.SAVED_CHATS}_${
                            sharedPreferencesManager.getString(
                                SharedPreferencesManager.SENDER_ADDRESS
                            )
                        }")
                    val savedChatsPrefs =
                        Gson().fromJson(savedChats, BluetoothChat::class.java)
                    var allChats: List<BluetoothMessage> = emptyList()
                    savedChatsPrefs?.let { allChats = savedChatsPrefs.message }
                    _state.update {
                        it.copy(
                            isConnected = true,
                            isConnecting = false,
                            errorMessage = null,
                            messages = allChats
                        )
                    }
                }

                is ConnectionResult.TransferSucceeded -> {
                    _state.update {
                        it.copy(
                            messages = it.messages + result.message
                        )
                    }
                    val senderMessages = _state.value.messages.filter { !it.isFromLocalUser }
                    sharedPreferencesManager.saveString(
                        "${SharedPreferencesManager.SENDERNAME}_${
                            sharedPreferencesManager.getString(
                                SharedPreferencesManager.SENDER_ADDRESS
                            )
                        }",
                        senderMessages.last().senderName
                    )
                    val gson = Gson()
                    val bluetoothChat = BluetoothChat(_state.value.messages)
                    val jsonData = gson.toJson(bluetoothChat)
                    sharedPreferencesManager.saveString(
                        "${SharedPreferencesManager.SAVED_CHATS}_${
                            sharedPreferencesManager.getString(
                                SharedPreferencesManager.SENDER_ADDRESS
                            )
                        }",
                        jsonData
                    )
                }

                is ConnectionResult.Error -> {
                    _state.update {
                        it.copy(
                            isConnected = false,
                            isConnecting = false,
                            errorMessage = result.message
                        )
                    }
                }

                else -> {}
            }
        }
            .catch { throwable ->
                bluetoothController.closeConnection()
                _state.update {
                    it.copy(
                        isConnected = false,
                        isConnecting = false,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothController.release()
    }

    fun saveProfileData(userData: String) {
        if (userData.isNotEmpty() && userData.isNotBlank()) {
            sharedPreferencesManager.saveString(SharedPreferencesManager.USERNAME, userData)
            Toast.makeText(context, "Username saved", Toast.LENGTH_LONG).show()
        } else
            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
    }

    fun getSavedSenderProfileDataFromPrefs(device: BluetoothDevice): String {
//        device.address?.let { deviceAddress = it }
        sharedPreferencesManager.getString(
            "${SharedPreferencesManager.SENDERNAME}_${
                sharedPreferencesManager.getString(
                    SharedPreferencesManager.SENDER_ADDRESS
                )
            }"
        ).let {
            if (it.isNotBlank() && it.isNotEmpty()) {
                return it
            }
        }
        return device.name ?: "User"
    }

    fun getSavedUserProfileDataFromPrefs(): String {
        return sharedPreferencesManager.getString(SharedPreferencesManager.USERNAME)
    }

    fun handleGeneralBackupClick() {
        if (networkHelper.isNetworkConnected()) {
            val firebaseDatabase = FirebaseDatabase.getInstance()
            val databaseReference = firebaseDatabase.getReference("BluetoothChats")
            val userAddress =
                sharedPreferencesManager.getString(SharedPreferencesManager.USER_ADDRESS)
            val userName = sharedPreferencesManager.getString(SharedPreferencesManager.USERNAME)
            var bluetoothChatToSenderAddressList: List<BluetoothChatToSenderAddress> = emptyList()
            val savedJsonData =
                sharedPreferencesManager.getString(SharedPreferencesManager.SAVED_DEVICES)
            val savedDevicesPrefs =
                Gson().fromJson(savedJsonData, BluetoothDeviceList::class.java)
            savedDevicesPrefs?.let {
                savedDevicesPrefs.listDevices.forEach { device ->
                    val savedChats =
                        sharedPreferencesManager.getString("${SharedPreferencesManager.SAVED_CHATS}_${device.address}")
                    val savedChatsPrefs =
                        Gson().fromJson(savedChats, BluetoothChat::class.java)
                    savedChatsPrefs?.let {
                        bluetoothChatToSenderAddressList =
                            bluetoothChatToSenderAddressList.plusElement(
                                BluetoothChatToSenderAddress(
                                    device.address,
                                    device.name, savedChatsPrefs
                                )
                            )
                    }
                }
            }
            val bluetoothChatToUserAddress =
                BluetoothChatToUserAddress(userAddress, userName, bluetoothChatToSenderAddressList)
            databaseReference.child("${userAddress}_${Build.MODEL ?: ""}")
                .setValue(bluetoothChatToUserAddress)
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Toast.makeText(context, "Backup completed successfully", Toast.LENGTH_SHORT)
                        .show()

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Backup Failed", Toast.LENGTH_SHORT).show()
                }
            })

        } else {
            Toast.makeText(context, "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }
}