package com.andela.presentation

data class CurrencyConverterViewState(
    val availableCurrenciesList: List<String> = listOf(),
    val currentExchangeRate: Double = 1.0,
    val toAmount: Double = 1.0
)
