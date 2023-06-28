package com.example.workoutstest.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import com.example.workoutstest.BuildConfig

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        configTimber()
    }

    private fun configTimber() {
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}