package com.videoeditor.pro.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DbClip::class],
    version = 1,
    exportSchema = false
)
abstract class TimelineDatabase : RoomDatabase() {
    abstract fun clipDao(): ClipDao
}

