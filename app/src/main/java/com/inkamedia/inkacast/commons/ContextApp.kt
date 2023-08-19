package com.inkamedia.inkacast.commons

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.fraporitmos.gdrivevideoplayer.repository.WebpageDatabase
import com.inkamedia.inkacast.utils.SharedPrefApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ContextApp: Application() {
    companion object {
        lateinit var instanceDatabase: WebpageDatabase
        lateinit var context : SharedPrefApp
        @get:Synchronized
        lateinit var instance : ContextApp
    }

    override fun onCreate() {
        super.onCreate()
        context = SharedPrefApp(applicationContext)

        instance = this
        instanceDatabase = Room.databaseBuilder(
            this, WebpageDatabase::class.java, "WebpageDatabase"
        ).build()
        if(context.getDarkMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}