package com.andela.data.datasource

import com.andela.data.datasource.model.CurrenciesDataModel.Currencies
import com.andela.data.datasource.model.CurrenciesDataModel.ApiError
import com.andela.data.datasource.model.CurrenciesDataModel.NetworkException
import com.andela.data.datasource.model.ExchangeRatesDataModel
import com.andela.data.service.CurrencyService
import com.haroldadmin.cnradapter.NetworkResponse.Success
import com.haroldadmin.cnradapter.NetworkResponse.ServerError
import com.haroldadmin.cnradapter.NetworkResponse.NetworkError
import com.haroldadmin.cnradapter.NetworkResponse.UnknownError
import retrofit2.HttpException
import java.io.IOException

class CurrencyDataSource constructor(
    private val service: CurrencyService
) : CurrencySource {

    override suspend fun fetchCurrencies() = try {
        when (val currenciesResponse = service.getCurrencies()) {
            is Success -> {
                when {
                    currenciesResponse.body.success -> Currencies(

                        currencies = currenciesResponse.body.symbols
                    )
                    else -> ApiError(message = currenciesResponse.body.error.info)
                }
            }
            is ServerError -> ApiError(message = currenciesResponse.body?.message ?: "Unknown Error")
            is NetworkError -> ApiError(message = currenciesResponse.error.message ?: "Unknown Error")
            is UnknownError -> ApiError(message = currenciesResponse.error.message ?: "Unknown Error")
        }
    } catch (exception: HttpException) {
        ApiError(message = exception.message())
    } catch (exception: IOException) {
        ApiError(message = exception.message ?: "Unknown Error")
    } catch (exception: Exception) {
        NetworkException
    }

    override suspend fun fetchExchangeRate(base: String, symbol: String) = try {
        when (val currenciesResponse = service.getExchangeRate(base, symbol)) {
            is Success -> {
                when {
                    currenciesResponse.body.success -> ExchangeRatesDataModel.ExchangeRates(
                        rates = currenciesResponse.body.rates
                    )
                    else -> ExchangeRatesDataModel.ApiError(message = currenciesResponse.body.error.info)
                }
            }
            is ServerError -> ExchangeRatesDataModel.ApiError(message = currenciesResponse.body?.message ?: "Unknown Error")
            is NetworkError -> ExchangeRatesDataModel.ApiError(message = currenciesResponse.error.message ?: "Unknown Error")
            is UnknownError -> ExchangeRatesDataModel.ApiError(message = currenciesResponse.error.message ?: "Unknown Error")
        }
    } catch (exception: HttpException) {
        ExchangeRatesDataModel.ApiError(message = exception.message())
    } catch (exception: IOException) {
        ExchangeRatesDataModel.ApiError(message = exception.message ?: "Unknown Error")
    } catch (exception: Exception) {
        ExchangeRatesDataModel.NetworkException
    }
}
