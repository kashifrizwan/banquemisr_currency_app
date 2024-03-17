package com.andela.presentation

data class CurrencyConverterViewState(
    val currenciesList: List<String> = listOf(),
    val exchangeRateForSelectedCurrencies: Double = 1.0,
    val isLoading: Boolean = false
)
