package com.andela.data.repository

import com.andela.data.datasource.CurrencySource
import com.andela.data.datasource.model.CurrenciesDataModel
import com.andela.data.datasource.model.ExchangeRatesDataModel
import com.andela.domain.model.CurrenciesDomainModel
import com.andela.domain.model.CurrenciesDomainModel.Currencies
import com.andela.domain.model.ExchangeRatesDomainModel
import com.andela.domain.model.ExchangeRatesDomainModel.ExchangeRatesSuccess
import com.andela.domain.repository.CurrencyRepository

class CurrencyDataRepository constructor(
    private val dataSource: CurrencySource
) : CurrencyRepository {

    override suspend fun fetchCurrencies() = when (val dataModel = dataSource.fetchCurrencies()) {
        is CurrenciesDataModel.Currencies -> Currencies(dataModel.currencies)
        is CurrenciesDataModel.ApiError -> CurrenciesDomainModel.Error(dataModel.message)
        is CurrenciesDataModel.NetworkException -> CurrenciesDomainModel.Error("Unknown Network Error!")
    }

    override suspend fun fetchExchangeRates(
        fromCurrency: String, toCurrency: String
    ) = when (val dataModel = dataSource.fetchExchangeRate(fromCurrency, toCurrency)) {
        is ExchangeRatesDataModel.ExchangeRates -> ExchangeRatesSuccess(dataModel.rates)
        is ExchangeRatesDataModel.ApiError -> ExchangeRatesDomainModel.Error(dataModel.message)
        is ExchangeRatesDataModel.NetworkException -> ExchangeRatesDomainModel.Error("Unknown Network Error!")
    }
}
