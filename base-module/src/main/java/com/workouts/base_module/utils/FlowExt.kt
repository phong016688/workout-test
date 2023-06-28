package com.workouts.base_module.utils

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

@OptIn(FlowPreview::class)
fun <T : Any, R : Any> suspendAsFlow(fromApi: suspend () -> T, mapper: ((T) -> R)): Flow<R> {
    return suspend { fromApi() }.asFlow().map(mapper)
}

@OptIn(FlowPreview::class)
fun <T : Any> suspendAsFlow(fromApi: suspend () -> T): Flow<T> {
    return suspend { fromApi() }.asFlow()
}