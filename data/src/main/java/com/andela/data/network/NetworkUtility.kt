package com.andela.data.network

import com.andela.data.service.model.GenericApiErrorModel
import com.andela.domain.abstraction.exception.UnknownNetworkException
import com.andela.domain.abstraction.exception.UseCaseCancellationException
import com.andela.domain.utility.globalLogger
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.NetworkResponse.Success
import com.haroldadmin.cnradapter.NetworkResponse.ServerError
import com.haroldadmin.cnradapter.NetworkResponse.NetworkError
import com.haroldadmin.cnradapter.NetworkResponse.UnknownError
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

inline fun <DATA_MODEL: Any> safeCall(
    apiCall: () -> NetworkResponse<DATA_MODEL, GenericApiErrorModel>
): DATA_MODEL {
    return try {
        val response = apiCall()
        globalLogger(response.toString())
        when (response) {
            is Success -> response.body
            is ServerError -> throw UnknownNetworkException(response.body?.message ?: "Unknown Error!")
            is NetworkError -> throw UnknownNetworkException(response.error.message ?: "Unknown Error!")
            is UnknownError -> throw UnknownNetworkException(response.error.message ?: "Unknown Error!")
        }
    } catch (exception: CancellationException) {
        throw UseCaseCancellationException(exception.message ?: "Unknown Error!")
    } catch (exception: HttpException) {
        throw UnknownNetworkException(exception.message())
    } catch (exception: IOException) {
        throw UnknownNetworkException(exception.message ?: "Unknown Error!")
    } catch (exception: Exception) {
        throw UnknownNetworkException(exception.message ?: "Unknown Error!")
    }
}
