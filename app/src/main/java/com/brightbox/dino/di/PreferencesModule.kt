package com.brightbox.dino.di

import android.content.Context
import com.brightbox.dino.data.preferences.PreferencesImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object PreferencesModule {

    @Provides
    @Singleton
    fun providePreferencesImpl(
        @ApplicationContext context: Context
    ): PreferencesImpl {
        return PreferencesImpl(context)
    }
}