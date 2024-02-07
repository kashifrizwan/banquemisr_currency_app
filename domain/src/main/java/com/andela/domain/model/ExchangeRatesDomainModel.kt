package com.andela.domain.model

sealed class ExchangeRatesDomainModel {
    data class ExchangeRatesSuccess(val rates: HashMap<String, Double>): ExchangeRatesDomainModel()
    data class Error(val message: String) : ExchangeRatesDomainModel()
}
