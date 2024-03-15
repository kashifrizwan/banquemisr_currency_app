package com.andela.domain.currencyexchange.model

data class ExchangeRatesRequestDomainModel(
    val base: String,
    val symbol: String
)
