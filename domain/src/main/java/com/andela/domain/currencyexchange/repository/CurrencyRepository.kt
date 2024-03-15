package com.andela.domain.currencyexchange.repository

import com.andela.domain.currencyexchange.model.CurrenciesDomainModel
import com.andela.domain.currencyexchange.model.ExchangeRatesDomainModel

interface CurrencyRepository {
    suspend fun fetchCurrencies(): CurrenciesDomainModel
    suspend fun fetchExchangeRates(fromCurrency: String, toCurrency: String): ExchangeRatesDomainModel
}
