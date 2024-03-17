package com.andela.data.datasource

import com.andela.data.datasource.model.CurrenciesDataModel
import com.andela.data.datasource.model.ExchangeRatesDataModel

interface CurrencySource {
    suspend fun fetchCurrencies(): CurrenciesDataModel
    suspend fun fetchExchangeRate(base: String, symbol: String): ExchangeRatesDataModel
}
