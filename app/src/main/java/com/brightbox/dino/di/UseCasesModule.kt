package com.brightbox.dino.di

import android.content.Context
import com.brightbox.dino.config.DinoDatabase
import com.brightbox.dino.data.preferences.PreferencesImpl
import com.brightbox.dino.usecases.ApplicationsUseCase
import com.brightbox.dino.usecases.CategoriesUseCase
import com.brightbox.dino.usecases.HabitsLogsUseCase
import com.brightbox.dino.usecases.HabitsUseCase
import com.brightbox.dino.usecases.LimitsUseCase
import com.brightbox.dino.usecases.PreferencesUseCase
import com.brightbox.dino.usecases.TasksUseCase
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
        db: DinoDatabase,
        @ApplicationContext applicationContext: Context
    ): ApplicationsUseCase {
        return ApplicationsUseCase(db, applicationContext)
    }

    @Singleton
    @Provides
    fun provideTasksUseCase(db: DinoDatabase): TasksUseCase {
        return TasksUseCase(db)
    }

    @Singleton
    @Provides
    fun provideCategoriesUseCase(db: DinoDatabase): CategoriesUseCase {
        return CategoriesUseCase(db)
    }

    @Singleton
    @Provides
    fun provideHabitsUseCase(db: DinoDatabase): HabitsUseCase {
        return HabitsUseCase(db)
    }

    @Singleton
    @Provides
    fun provideHabitsLogsUseCase(db: DinoDatabase): HabitsLogsUseCase {
        return HabitsLogsUseCase(db)
    }

    @Provides
    @Singleton
    fun providePreferencesUseCase(preferencesImpl: PreferencesImpl): PreferencesUseCase {
        return PreferencesUseCase(preferencesImpl)
    }

    @Provides
    @Singleton
    fun provideApplicationLimitsUseCase(
        @ApplicationContext applicationContext: Context,
        db: DinoDatabase
    ): LimitsUseCase {
        return LimitsUseCase(applicationContext, db)
    }
}