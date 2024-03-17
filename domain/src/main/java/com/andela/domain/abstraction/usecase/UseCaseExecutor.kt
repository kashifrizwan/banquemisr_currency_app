package com.andela.domain.abstraction.usecase

import com.andela.domain.abstraction.exception.UnknownNetworkException
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
            useCase.run(request, callback)
        } catch (cancellationException: CancellationException) {
            //onError(cancellationException.message ?: "Unexpected Error occurred!")
        } catch (unknownNetworkException: UnknownNetworkException) {
            onError(unknownNetworkException.errorMessage)
        } catch (throwable: Throwable) {
            onError(throwable.message ?: "Unexpected Error occurred!")
        } catch (exception: Exception) {
            onError(exception.message ?: "Unexpected Error occurred!")
        }
    }
}
