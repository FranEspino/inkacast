package com.inkamedia.inkacast.utils

import android.content.Context

class SharedPrefApp(val context: Context) {
    val SHARED_NAME = "context_app_inkacast"
    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    //DARK MODE
    val DARK_MODE = "DARK_MODE"
    fun setDarkMode(darkMode:Boolean) =  storage.edit().putBoolean(DARK_MODE, darkMode).apply()
    fun getDarkMode():Boolean = storage.getBoolean(DARK_MODE, false)

    //IS DEVICE CONNECTED
    val IS_DEVICE_CONNECTED = "IS_DEVICE_CONNECTED"
    fun setIsDeviceConnected(isDeviceConnected:Boolean) =  storage.edit().putBoolean(IS_DEVICE_CONNECTED, isDeviceConnected).apply()
    fun getIsDeviceConnected():Boolean = storage.getBoolean(IS_DEVICE_CONNECTED, false)

    //DEVICE NAME
    val SHOWCASE = "SHOWCASE"
    fun setShowCase(showcase:Boolean) =  storage.edit().putBoolean(SHOWCASE, showcase).apply()
    fun getShowCase():Boolean = storage.getBoolean(SHOWCASE, false)!!

}