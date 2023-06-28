package com.example.workoutstest.ui

import androidx.lifecycle.viewModelScope
import com.example.workoutstest.domain.repository.WorkoutRepository
import com.workouts.base_module.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : BaseViewModel() {

    val workoutState = merge(
        flowOf(workoutRepository.getDataLocal() ?: emptyList()),
        workoutRepository.fetchWorkouts()
            .onEach { workoutRepository.saveDataLocal(it) }
            .catch { Timber.e(it.message) }
    ).shareIn(viewModelScope, SharingStarted.Lazily, 1)

}