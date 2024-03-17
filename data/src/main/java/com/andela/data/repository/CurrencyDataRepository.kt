package com.andela.data.repository

import com.andela.data.datasource.CurrencySource
import com.andela.domain.currencyexchange.model.CurrenciesDomainModel
import com.andela.domain.currencyexchange.model.ExchangeRatesDomainModel
import com.andela.domain.currencyexchange.repository.CurrencyRepository

class CurrencyDataRepository constructor(
    private val dataSource: CurrencySource
) : CurrencyRepository {

    override suspend fun fetchCurrencies() =
        CurrenciesDomainModel(dataSource.fetchCurrencies().currencies)

    override suspend fun fetchExchangeRates(
        fromCurrency: String, toCurrency: String
    ) = ExchangeRatesDomainModel(dataSource.fetchExchangeRate(fromCurrency, toCurrency).rates)
}
