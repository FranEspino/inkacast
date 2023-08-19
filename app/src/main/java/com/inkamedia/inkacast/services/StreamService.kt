package com.inkamedia.inkacast.services


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.core.app.NotificationCompat

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.mediarouter.app.MediaRouteButton
import com.digenio.inkacast.Chromecast
import com.inkamedia.inkacast.commons.Constants.ACTION_SERVICE_START
import com.inkamedia.inkacast.commons.Constants.ACTION_SERVICE_STOP
import com.inkamedia.inkacast.commons.Constants.NOTIFICATION_CHANNEL_ID
import com.inkamedia.inkacast.commons.Constants.NOTIFICATION_CHANNEL_NAME
import com.inkamedia.inkacast.commons.Constants.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class StreamService : LifecycleService() {

    @Inject
    lateinit var notification: NotificationCompat.Builder
    @Inject
    lateinit var notificationManager: NotificationManager
    private var handler: Handler = Handler()
    private var isCounterRunning = false
    private var counterValue = 0
    override fun onCreate() {
        super.onCreate()


        createNotificationChannel()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int ): Int {
        intent?.let {
            when (it.action) {
                ACTION_SERVICE_START -> {

                    if (!isCounterRunning) {
                        isCounterRunning = true
                        startForegroundService()
                        startCounter()
                    }
                }
                ACTION_SERVICE_STOP -> {
                    if (isCounterRunning) {
                        isCounterRunning = false
                        stopForegroundService()
                    }
                }
                else -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        startForeground(NOTIFICATION_ID, notification.build())

    }

    private fun startCounter() {
        handler.post(object : Runnable {
            override fun run() {
                updateNotificationPeriodically()
                if (isCounterRunning) {
                    handler.postDelayed(this, 1000)
                    counterValue++
                }
            }
        })
    }

    private fun stopForegroundService() {
        isCounterRunning = false
        handler.removeCallbacksAndMessages(null)
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(true)
        stopSelf()
    }

    private fun updateNotificationPeriodically() {
        notification.apply {
            setContentTitle("Counter")
            setContentText(counterValue.toString())
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

}
