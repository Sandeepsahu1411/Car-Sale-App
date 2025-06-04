package com.example.salecar.di

import android.app.Application
import android.content.Context
import com.example.salecar.doman_layer.Repo
import com.example.salecar.internet_check.AndroidConnectivityObserver

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

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context) = context as Application

    @Singleton
    @Provides
    fun provideRepo() = Repo()

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
//
//    @Singleton
//    @Provides
//    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
//        return NetworkConnectivityObserver(context)
//    }

    @Singleton
    @Provides
    fun provideNetworkConnectivityService(@ApplicationContext context: Context): AndroidConnectivityObserver{
        return AndroidConnectivityObserver(context)
    }

    @Singleton
    @Provides
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }


}