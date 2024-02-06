package com.andela.data.service

import com.andela.data.service.model.CurrenciesApiModel
import com.andela.data.service.model.ExchangeRateApiModel
import com.andela.data.service.model.GenericApiErrorModel
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val QUERY_PARAM_BASE = "base"
private const val QUERY_PARAM_SYMBOLS = "symbols"

interface CurrencyService {
    @GET("symbols")
    suspend fun getCurrencies(): NetworkResponse<CurrenciesApiModel, GenericApiErrorModel>

    @GET("latest")
    suspend fun getExchangeRate(
        @Query(QUERY_PARAM_BASE) base: String,
        @Query(QUERY_PARAM_SYMBOLS) symbols: String
    ): NetworkResponse<ExchangeRateApiModel, GenericApiErrorModel>
}
