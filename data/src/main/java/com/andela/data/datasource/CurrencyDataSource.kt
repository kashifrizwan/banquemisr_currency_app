package com.andela.data.datasource

import com.andela.data.network.safeCall
import com.andela.data.service.CurrencyService
import com.andela.data.service.mapper.CurrenciesApiToDataModelMapper
import com.andela.data.service.mapper.ExchangeRatesApiToDataModelMapper

class CurrencyDataSource constructor(
    private val service: CurrencyService,
    private val currenciesApiToDataModelMapper: CurrenciesApiToDataModelMapper,
    private val exchangeRatesApiToDataModelMapper: ExchangeRatesApiToDataModelMapper
) : CurrencySource {

    override suspend fun fetchCurrencies() =
        currenciesApiToDataModelMapper.map(safeCall { service.getCurrencies() })

    override suspend fun fetchExchangeRate(base: String, symbol: String) =
        exchangeRatesApiToDataModelMapper.map(safeCall { service.getExchangeRate(base, symbol) })
}
