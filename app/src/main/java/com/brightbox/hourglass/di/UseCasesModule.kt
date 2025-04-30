package com.brightbox.hourglass.di

import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.usecases.CategoriesUseCase
import com.brightbox.hourglass.usecases.TasksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Singleton
    @Provides
    fun provideTasksUseCase(db: HourglassDatabase): TasksUseCase {
        return TasksUseCase(db)
    }

    @Singleton
    @Provides
    fun provideCategoriesUseCase(db: HourglassDatabase): CategoriesUseCase {
        return CategoriesUseCase(db)
    }
}