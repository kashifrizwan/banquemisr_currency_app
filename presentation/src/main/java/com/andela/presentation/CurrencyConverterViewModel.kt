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

    override fun initialState() = CurrencyConverterViewState()

    private var getCurrenciesJob: Job? = null
    private var getExchangeRatesJob: Job? = null

    fun onFragmentViewCreated() {
        if (getCurrenciesJob?.isActive == true) { getCurrenciesJob?.cancel() }
        getCurrenciesJob = viewModelScope.launch {
            updateProgressBarVisibility(isVisible = true)
            when (val result = getCurrenciesUseCase.execute()) {
                is Currencies -> updateViewState(
                    currentViewState().copy(
                        isLoading = false,
                        availableCurrenciesList = result.currencies.keys.toList().sorted()
                    )
                )
                is CurrenciesDomainModel.Error -> {
                    updateProgressBarVisibility(isVisible = false)
                    notifyDialogCommand(result.message)
                }
            }
        }
    }

    fun onCurrencyChangedAction(fromCurrency: String, toCurrency: String) {
        if (fromCurrency == toCurrency || fromCurrency.isEmpty() || toCurrency.isEmpty()) {
            return
        }
        if (getExchangeRatesJob?.isActive == true) { getExchangeRatesJob?.cancel() }
        getExchangeRatesJob = viewModelScope.launch {
            updateProgressBarVisibility(isVisible = true)
            when (
                val response = getExchangeRatesUseCase.execute(
                    request = ExchangeRatesRequestDomainModel(fromCurrency, toCurrency)
                )
            ) {
                is ExchangeRatesSuccess -> updateViewState(
                    currentViewState().copy(
                        isLoading = false,
                        exchangeRateForSelectedCurrencies = response.rates[toCurrency] ?: 1.0
                    )
                )
                is ExchangeRatesDomainModel.Error -> {
                    updateProgressBarVisibility(isVisible = false)
                    notifyDialogCommand(response.message)
                }
            }
        }
    }

    fun onInputAmountChangedAction(inputAmount: Double, isReverse: Boolean): Double {
        val calculatedRate = if (isReverse) {
            inputAmount / (currentViewState().exchangeRateForSelectedCurrencies)
        } else {
            inputAmount * (currentViewState().exchangeRateForSelectedCurrencies)
        }
        return String.format("%.3f", calculatedRate).toDouble()
    }

    fun onCurrenciesSwappedAction() {
        updateViewState(
            currentViewState().copy(
                exchangeRateForSelectedCurrencies = 1 / (currentViewState().exchangeRateForSelectedCurrencies)
            )
        )
    }

    private fun updateProgressBarVisibility(isVisible: Boolean) {
        updateViewState(
            currentViewState().copy(
                isLoading = isVisible
            )
        )
    }
}
