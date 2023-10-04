package com.inkacast.gdrivevideoplayer.repository

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Webpage::class],
    version = 1,
    exportSchema = false)
abstract class WebpageDatabase: RoomDatabase() {
    abstract fun webPageDao(): WebpageDao
}
