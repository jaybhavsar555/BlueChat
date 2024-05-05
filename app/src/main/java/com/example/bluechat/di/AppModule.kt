package com.example.bluechat.di

import android.content.Context
import com.example.bluechat.data.chat.AndroidBluetoothController
import com.example.bluechat.domain.chat.BluetoothController
import com.example.bluechat.utils.network.NetworkHelper
import com.example.bluechat.utils.prefs.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothController(
        @ApplicationContext context: Context,
        sharedPreferencesManager: SharedPreferencesManager
    ): BluetoothController {
        return AndroidBluetoothController(context, sharedPreferencesManager)
    }

    @Singleton
    @Provides
    fun provideSharedPreferencesManager(@ApplicationContext context: Context): SharedPreferencesManager {
        return SharedPreferencesManager.getInstance(context)
    }
    @Singleton
    @Provides
    fun provideNetworkHelper(@ApplicationContext context: Context) = NetworkHelper(context)
}