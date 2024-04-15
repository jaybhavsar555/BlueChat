package com.example.bluechat.utils.prefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPreferencesManager private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("BlueChat", Context.MODE_PRIVATE)

    companion object {
        private var instance: SharedPreferencesManager? = null
        fun getInstance(context: Context): SharedPreferencesManager {
            return instance ?: SharedPreferencesManager(context).also { instance = it }

        }

        const val USERNAME = "USERNAME"
        const val SENDERNAME = "SENDERNAME"
        const val NO_FRESH_INSTALL = "NO_FRESH_INSTALL"
        const val SAVED_DEVICES = "SAVED_DEVICES"
    }

    fun saveString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int = 1): Int {
        return prefs.getInt(key, defaultValue)
    }

    fun saveInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun saveBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }
}