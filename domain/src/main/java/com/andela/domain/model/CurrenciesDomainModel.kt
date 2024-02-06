package com.andela.domain.model

sealed class CurrenciesDomainModel {
    data class Currencies(val currencies: HashMap<String, String>): CurrenciesDomainModel()
    data class Error(val message: String) : CurrenciesDomainModel()
}
