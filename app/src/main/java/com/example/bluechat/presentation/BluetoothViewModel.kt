package com.example.bluechat.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bluechat.data.chat.toBluetoothDeviceDomain
import com.example.bluechat.domain.chat.BluetoothController
import com.example.bluechat.domain.chat.BluetoothDevice
import com.example.bluechat.domain.chat.BluetoothDeviceDomain
import com.example.bluechat.domain.chat.ConnectionResult
import com.example.bluechat.utils.prefs.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val bluetoothController: BluetoothController,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val chatListDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())

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
    }

    fun connectToDevice(device: BluetoothDeviceDomain) {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController
            .connectToDevice(device)
            .listen()
    }

    fun addDeviceToChatList(newDevice: BluetoothDevice) {
        _state.update {
            if (newDevice in it.chatListDevices) it else it.copy(
                chatListDevices = it.chatListDevices.plusElement(
                    newDevice
                )
            )
        }
        _state.update { it.copy(isDeviceAddedToChatList = true) }
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
                    _state.update {
                        it.copy(
                            isConnected = true,
                            isConnecting = false,
                            errorMessage = null
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
                        SharedPreferencesManager.SENDERNAME, senderMessages.last().senderName
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
//        bluetoothController.release()
    }

    fun saveProfileData(userData: String) {
        sharedPreferencesManager.saveString(SharedPreferencesManager.USERNAME, userData)
    }

    fun handleOnOff(isOn: Boolean) {
        if (isOn)
            _state.update { it.copy(isOn = true) }
    }

    fun handleProfileClick() {
        _state.update { it.copy(openProfileScreen = true) }
        Log.d("abhi", "click : ${state.value.openProfileScreen}")
    }

    fun handleGoToAllDevicesClick() {
        _state.update { it.copy(openAllDeviceScreen = true) }
    }

    fun handleGeneralBackupClick() {

    }

    fun handleSingleBackupClick() {

    }

    fun getSavedProfileDataFromPrefs(): String {
        return sharedPreferencesManager.getString(SharedPreferencesManager.SENDERNAME)
    }
}