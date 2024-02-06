package com.andela.data.datasource.model

sealed class ExchangeRatesDataModel {
    data class ExchangeRates(val rates: HashMap<String, Double>): ExchangeRatesDataModel()
    data class ApiError(val message: String) : ExchangeRatesDataModel()
    object NetworkException : ExchangeRatesDataModel()
}
