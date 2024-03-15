package com.andela.domain.abstraction.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseUseCase<INPUT_TYPE, OUTPUT_TYPE> {

    internal suspend fun run(request: INPUT_TYPE, callback: (OUTPUT_TYPE) -> Unit) {
        val result = withContext(Dispatchers.Default) {
            executeUseCase(request)
        }
        callback(result)
    }

    protected abstract suspend fun executeUseCase(request: INPUT_TYPE): OUTPUT_TYPE
}
