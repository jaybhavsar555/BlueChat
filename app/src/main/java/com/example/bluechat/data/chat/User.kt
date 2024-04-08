package com.example.bluechat.data.chat

import android.graphics.drawable.Icon
import java.io.Serializable

data class User(
    val username: String?,
    val profilePicture: Icon?


):Serializable