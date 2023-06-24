package com.inkamedia.inkacast

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.inkamedia.inkacast.utils.SharedPreferenceApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ContextApp: Application() {

    companion object {
        lateinit var context : SharedPreferenceApp
        @get:Synchronized
        lateinit var instance : ContextApp
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        context = SharedPreferenceApp(applicationContext)
        if (ContextApp.context.getDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
//    fun setConnectionListener(listener: BaseActivity.ConnectionReceiverListener){
//        BaseActivity.connectionReceiverListener = listener
//    }
}