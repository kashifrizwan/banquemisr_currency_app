package com.andela.presentation

import androidx.lifecycle.viewModelScope
import com.andela.domain.model.CurrenciesDomainModel
import com.andela.domain.model.CurrenciesDomainModel.Currencies
import com.andela.domain.model.ExchangeRatesDomainModel
import com.andela.domain.model.ExchangeRatesDomainModel.ExchangeRatesSuccess
import com.andela.domain.model.ExchangeRatesRequestDomainModel
import com.andela.domain.usecases.GetCurrenciesUseCase
import com.andela.domain.usecases.GetExchangeRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase
) : BaseViewModel<CurrencyConverterViewState>() {

    private var getCurrenciesJob: Job? = null
    private var getExchangeRates: Job? = null

    fun onFragmentCreated() {
        if (getCurrenciesJob?.isActive == true) { getCurrenciesJob?.cancel() }
        getCurrenciesJob = viewModelScope.launch {
            when (val result = getCurrenciesUseCase.execute()) {
                is Currencies -> updateViewState(
                    CurrencyConverterViewState(
                        availableCurrenciesList = result.currencies.keys.toList().sorted()
                    )
                )
                is CurrenciesDomainModel.Error -> notifyDialogCommand(result.message)
            }
        }
    }

    fun onCurrencyChangedAction(fromCurrency: String, toCurrency: String) {
        if (getExchangeRates?.isActive == true) { getExchangeRates?.cancel() }
        getExchangeRates = viewModelScope.launch {
            when (
                val response = getExchangeRatesUseCase.execute(
                    request = ExchangeRatesRequestDomainModel(fromCurrency, toCurrency)
                )
            ) {
                is ExchangeRatesSuccess -> updateExchangeRateForSelectedCurrencies(
                    rate = response.rates[toCurrency] ?: 1.0
                )
                is ExchangeRatesDomainModel.Error -> notifyDialogCommand(response.message)
            }
        }
    }

    private fun updateExchangeRateForSelectedCurrencies(rate: Double) {
        updateViewState(
            currentViewState()?.copy(
                exchangeRateForSelectedCurrencies = rate
            )
        )
    }

    fun onInputAmountChangedAction(inputAmount: Double, isReverse: Boolean): Double {
        val calculatedRate = if (isReverse) {
            inputAmount / (currentViewState()?.exchangeRateForSelectedCurrencies ?: 1.0)
        } else {
            inputAmount * (currentViewState()?.exchangeRateForSelectedCurrencies ?: 1.0)
        }
        return String.format("%.3f", calculatedRate).toDouble()
    }

    fun onCurrenciesSwappedAction() {
        val currentRate = currentViewState()?.exchangeRateForSelectedCurrencies ?: 1.0
        val reversedRate = 1/currentRate
        updateViewState(
            currentViewState()?.copy(
                exchangeRateForSelectedCurrencies = reversedRate
            )
        )
    }
}
