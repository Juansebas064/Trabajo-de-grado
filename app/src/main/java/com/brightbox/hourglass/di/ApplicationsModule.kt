package com.brightbox.hourglass.di

import android.content.Context
import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.usecases.ApplicationsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationsModule {

    @Singleton
    @Provides
    fun provideApplicationsUseCase(
        db: HourglassDatabase,
        @ApplicationContext applicationContext: Context
    ): ApplicationsUseCase {
        return ApplicationsUseCase(db, applicationContext)
    }
}