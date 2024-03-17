package com.andela.domain.abstraction.usecase

import com.andela.domain.abstraction.usecase.UseCaseExecutor
import kotlinx.coroutines.CoroutineScope

typealias UseCaseExecutorProvider = (coroutineScope: CoroutineScope) -> UseCaseExecutor
