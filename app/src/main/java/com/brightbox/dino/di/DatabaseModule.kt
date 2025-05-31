package com.brightbox.dino.di

import android.content.Context
import androidx.room.Room
import com.brightbox.dino.config.DinoDatabase
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
    fun provideDinoDatabase(
        @ApplicationContext applicationContext: Context
    ): DinoDatabase {
        return Room.databaseBuilder(
            applicationContext,
            DinoDatabase::class.java,
            "dino_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }
}