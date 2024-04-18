package com.example.bluechat.domain.chat

data class BluetoothChatToUserAddress(
    val userAddress: String?,
    val userName: String?,
    val bluetoothChatToSenderAddress: List<BluetoothChatToSenderAddress>
)

data class BluetoothChatToSenderAddress(
    val senderAddress: String?,
    val senderName: String?,
    val bluetoothChat: BluetoothChat
)
