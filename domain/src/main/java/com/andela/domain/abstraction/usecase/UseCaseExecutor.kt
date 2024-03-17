package com.andela.domain.abstraction.usecase

import com.andela.domain.abstraction.exception.UnknownNetworkException
import com.andela.domain.abstraction.exception.UseCaseCancellationException
import com.andela.domain.utility.globalLogger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UseCaseExecutor(
    private val coroutineScope: CoroutineScope,
    jobProvider: () -> Job = { Job() }
) {
    private var job: Job = jobProvider()

    fun <OUTPUT_TYPE> execute(
        useCase: BaseUseCase<Unit, OUTPUT_TYPE>,
        callback: (OUTPUT_TYPE) -> Unit = {},
        onError: (message: String) -> Unit = {}
    ) = execute(useCase, Unit, callback, onError)

    fun <INPUT_TYPE, OUTPUT_TYPE> execute(
        useCase: BaseUseCase<INPUT_TYPE, OUTPUT_TYPE>,
        request: INPUT_TYPE,
        callback: (OUTPUT_TYPE) -> Unit = {},
        onError: (message: String) -> Unit = {}
    ) = launchUseCaseExecution(useCase, request, callback, onError)

    private fun <INPUT_TYPE, OUTPUT_TYPE> launchUseCaseExecution(
        useCase: BaseUseCase<INPUT_TYPE, OUTPUT_TYPE>,
        request: INPUT_TYPE,
        callback: (OUTPUT_TYPE) -> Unit = {},
        onError: (message: String) -> Unit = {}
    ) = coroutineScope.launch(job) {
        try {
            globalLogger("$useCase execution started")
            useCase.run(request, callback)
        } catch (exception: UseCaseCancellationException) {
            globalLogger("$useCase execution cancelled with message: ${exception.errorMessage}")
        } catch (exception: CancellationException) {
            globalLogger("$useCase execution cancelled with message: ${exception.message}")
        } catch (exception: UnknownNetworkException) {
            globalLogger(exception.message)
            onError(exception.errorMessage)
        } catch (throwable: Throwable) {
            globalLogger(throwable.message)
            onError(throwable.message ?: "Unexpected Error occurred!")
        } catch (exception: Exception) {
            globalLogger(exception.message)
            onError(exception.message ?: "Unexpected Error occurred!")
        }
    }
}
