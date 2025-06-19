package com.brightbox.dino.di

import android.content.Context
import com.brightbox.dino.config.DinoDatabase
import com.brightbox.dino.model.data.preferences.PreferencesImpl
import com.brightbox.dino.model.usecases.ApplicationsUseCase
import com.brightbox.dino.model.usecases.CategoriesUseCase
import com.brightbox.dino.model.usecases.HabitsLogsUseCase
import com.brightbox.dino.model.usecases.HabitsUseCase
import com.brightbox.dino.model.usecases.LimitsUseCase
import com.brightbox.dino.model.usecases.PreferencesUseCase
import com.brightbox.dino.model.usecases.TasksUseCase
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