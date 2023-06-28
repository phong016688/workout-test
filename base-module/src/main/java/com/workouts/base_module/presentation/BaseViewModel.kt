package com.workouts.base_module.presentation

import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseViewModel : ViewModel() {
    private val lazyJobs = lazy(LazyThreadSafetyMode.NONE) { HashSet<Job>() }
    private val jobs by lazyJobs
    private val _inputAction = MutableSharedFlow<BaseAction>()
    protected val inputAction: Flow<BaseAction> = _inputAction
    private val _singleEvent = MutableSharedFlow<BaseSingleEvent>(0, 1, BufferOverflow.DROP_OLDEST)
    val singleEvent: SharedFlow<BaseSingleEvent> = _singleEvent

    protected fun pushAction(action: BaseAction) {
        _inputAction.tryEmit(action)
    }

    protected fun pushSingleEvent(message: BaseSingleEvent) {
        _singleEvent.tryEmit(message)
    }

    @MainThread
    protected fun Job.addToJobDispose() = apply { jobs += this }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        if (lazyJobs.isInitialized()) {
            jobs.forEach { it.cancel() }
            jobs.clear()
        }
    }
}
