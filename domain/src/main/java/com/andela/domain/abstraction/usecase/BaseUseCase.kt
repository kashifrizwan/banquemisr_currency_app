package com.andela.domain.abstraction.usecase

import com.andela.domain.utility.globalLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseUseCase<INPUT_TYPE, OUTPUT_TYPE> {

    internal suspend fun run(request: INPUT_TYPE, callback: (OUTPUT_TYPE) -> Unit) {
        val result = withContext(Dispatchers.Default) {
            executeUseCase(request)
        }
        callback(result)
        globalLogger(this.toString() + " completed with response: " + result.toString())
    }

    internal abstract suspend fun executeUseCase(request: INPUT_TYPE): OUTPUT_TYPE
}
