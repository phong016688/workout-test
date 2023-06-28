package com.example.workoutstest.di

import android.content.Context
import com.example.workoutstest.data.local.prefrence.AppPreferencesImpl
import com.example.workoutstest.data.local.prefrence.AppSharedPreferences
import com.workouts.base_module.utils.Network
import com.workouts.base_module.utils.NetworkConnectivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity {
        return Network(context)
    }

    @Provides
    @Singleton
    fun provideNetworkAppPreferencesImpl(@ApplicationContext context: Context): AppSharedPreferences {
        return AppPreferencesImpl(context)
    }
}