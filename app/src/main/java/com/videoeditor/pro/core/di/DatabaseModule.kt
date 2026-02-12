package com.videoeditor.pro.core.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.videoeditor.pro.data.local.TimelineDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTimelineDatabase(
        @ApplicationContext context: Context
    ): TimelineDatabase {
        return Room.databaseBuilder(
            context,
            TimelineDatabase::class.java,
            "timeline.db"
        ).build()
    }

    @Provides
    fun provideClipDao(db: TimelineDatabase) = db.clipDao()

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}

