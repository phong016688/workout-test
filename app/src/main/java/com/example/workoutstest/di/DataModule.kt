package com.example.workoutstest.di

import com.example.workoutstest.data.remote.retofit.api.WorkoutApi
import com.example.workoutstest.data.remote.retofit.creator.RetrofitCreator
import com.example.workoutstest.data.remote.retofit.interceptor.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.workoutstest.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun retrofit(
        tokenInterceptor: TokenInterceptor
    ): RetrofitCreator {
        return RetrofitCreator(
            BuildConfig.BASE_URL,
            tokenInterceptor
        )
    }

    @Provides
    @Singleton
    fun providesAPI(creator: RetrofitCreator): WorkoutApi {
        return creator.createService(WorkoutApi::class.java)
    }
}