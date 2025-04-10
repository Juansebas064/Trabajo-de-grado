package com.brightbox.hourglass.di

import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.usecases.TasksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TasksModule {

    @Singleton
    @Provides
    fun provideTasksUseCase(db: HourglassDatabase): TasksUseCase {
        return TasksUseCase(db)
    }
}