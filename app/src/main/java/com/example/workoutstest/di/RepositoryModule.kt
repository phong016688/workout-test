package com.example.workoutstest.di

import com.example.workoutstest.data.repository.WorkoutRepositoryImpl
import com.example.workoutstest.domain.repository.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(repo: WorkoutRepositoryImpl): WorkoutRepository
}
