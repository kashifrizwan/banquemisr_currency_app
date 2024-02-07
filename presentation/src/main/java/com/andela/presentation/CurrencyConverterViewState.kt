package com.andela.presentation

data class CurrencyConverterViewState(
    val availableCurrenciesList: List<String> = listOf(),
    val exchangeRateForSelectedCurrencies: Double = 1.0
)
