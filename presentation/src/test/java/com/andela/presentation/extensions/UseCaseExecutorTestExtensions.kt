package com.andela.presentation.extensions

import com.andela.domain.abstraction.usecase.BaseUseCase
import com.andela.domain.abstraction.usecase.UseCaseExecutor
import kotlinx.coroutines.Job
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

fun <RESULT> UseCaseExecutor.givenSuccessfulUseCaseExecution(
    useCase: BaseUseCase<Unit, RESULT>,
    result: RESULT
) {
    doAnswer { invocationOnMock ->
        (invocationOnMock.arguments[1] as (RESULT) -> Unit)(result)
        mock<Job>()
    }.whenever(this).execute(
        useCase = eq(useCase),
        callback = any(),
        onError = any()
    )
}

fun <REQUEST, RESULT> UseCaseExecutor.givenSuccessfulUseCaseExecution(
    useCase: BaseUseCase<REQUEST, RESULT>,
    request: REQUEST,
    result: RESULT
) {
    doAnswer { invocationOnMock ->
        (invocationOnMock.arguments[2] as (RESULT) -> Unit)(result)
        mock<Job>()
    }.whenever(this).execute(
        useCase = eq(useCase),
        request = eq(request),
        callback = any(),
        onError = any()
    )
}

fun <RESULT, ERROR> UseCaseExecutor.givenFailedUseCaseExecution(
    useCase: BaseUseCase<Unit, RESULT>,
    error: ERROR
) {
    doAnswer { invocationOnMock ->
        (invocationOnMock.arguments[2] as (ERROR) -> Unit)(error)
        mock<Job>()
    }.whenever(this).execute(
        useCase = eq(useCase),
        callback = any(),
        onError = any()
    )
}

fun <REQUEST, RESULT, ERROR> UseCaseExecutor.givenFailedUseCaseExecution(
    useCase: BaseUseCase<REQUEST, RESULT>,
    request: REQUEST,
    error: ERROR
) {
    doAnswer { invocationOnMock ->
        (invocationOnMock.arguments[3] as (ERROR) -> Unit)(error)
        mock<Job>()
    }.whenever(this).execute(
        useCase = eq(useCase),
        request = eq(request),
        callback = any(),
        onError = any()
    )
}
