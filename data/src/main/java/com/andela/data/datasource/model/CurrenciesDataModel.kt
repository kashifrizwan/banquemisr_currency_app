package com.andela.data.datasource.model

sealed class CurrenciesDataModel {
    data class Currencies(val currencies: HashMap<String, String>): CurrenciesDataModel()
    data class ApiError(val message: String) : CurrenciesDataModel()
    object NetworkException : CurrenciesDataModel()
}
