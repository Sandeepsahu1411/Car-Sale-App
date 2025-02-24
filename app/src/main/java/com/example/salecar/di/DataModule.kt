package com.example.salecar.di

import android.app.Application
import android.content.Context
import com.example.salecar.preference_db.UserPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideUserPreferenceManager(@ApplicationContext context: Context) =
        UserPreferenceManager(context)
}