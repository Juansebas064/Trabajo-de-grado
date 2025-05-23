package com.brightbox.hourglass.di

import android.content.Context
import androidx.room.Room
import com.brightbox.hourglass.config.HourglassDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideHourglassDatabase(
        @ApplicationContext applicationContext: Context
    ): HourglassDatabase {
        return Room.databaseBuilder(
            applicationContext,
            HourglassDatabase::class.java,
            "hourglass_database"
        ).build()
    }
}