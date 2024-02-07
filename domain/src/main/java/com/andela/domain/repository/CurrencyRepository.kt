package com.andela.domain.repository

import com.andela.domain.model.CurrenciesDomainModel
import com.andela.domain.model.ExchangeRatesDomainModel

interface CurrencyRepository {
    suspend fun fetchCurrencies(): CurrenciesDomainModel
    suspend fun fetchExchangeRates(fromCurrency: String, toCurrency: String): ExchangeRatesDomainModel
}
