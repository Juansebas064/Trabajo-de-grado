package com.brightbox.hourglass.di

import android.content.Context
import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.data.preferences.PreferencesImpl
import com.brightbox.hourglass.usecases.ApplicationsUseCase
import com.brightbox.hourglass.usecases.CategoriesUseCase
import com.brightbox.hourglass.usecases.GeneralPreferencesUseCase
import com.brightbox.hourglass.usecases.TasksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Singleton
    @Provides
    fun provideApplicationsUseCase(
        db: HourglassDatabase,
        @ApplicationContext applicationContext: Context
    ): ApplicationsUseCase {
        return ApplicationsUseCase(db, applicationContext)
    }

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

    @Provides
    @Singleton
    fun providePreferencesUseCase(preferencesImpl: PreferencesImpl): GeneralPreferencesUseCase {
        return GeneralPreferencesUseCase(preferencesImpl)
    }
}