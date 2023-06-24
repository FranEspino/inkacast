package com.inkamedia.inkacast.utils

import android.content.Context

class SharedPreferenceApp(val context: Context) {
    val SHARED_NAME = "context_app_inkacast"
    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    //DARK MODE
    val DARK_MODE = "DARK_MODE"
    fun setDarkMode(darkMode:Boolean) =  storage.edit().putBoolean(DARK_MODE, darkMode).apply()
    fun getDarkMode():Boolean = storage.getBoolean(DARK_MODE, false)

}