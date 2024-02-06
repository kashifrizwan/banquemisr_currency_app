package com.andela.presentation

import androidx.lifecycle.viewModelScope
import com.andela.domain.model.CurrenciesDomainModel
import com.andela.domain.model.CurrenciesDomainModel.Currencies
import com.andela.domain.model.ExchangeRatesDomainModel
import com.andela.domain.model.ExchangeRatesDomainModel.ExchangeRates
import com.andela.domain.model.ExchangeRatesRequestDomainModel
import com.andela.domain.usecases.GetCurrenciesUseCase
import com.andela.domain.usecases.GetExchangeRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase
) : BaseViewModel<CurrencyConverterViewState>() {

    fun onFragmentCreated() {
        viewModelScope.launch {
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

    fun onCurrencyChangeAction(fromCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            when (
                val response = getExchangeRatesUseCase.execute(
                    request = ExchangeRatesRequestDomainModel(fromCurrency, toCurrency)
                )
            ) {
                is ExchangeRates -> updateViewState(
                    currentViewState()?.copy(
                        currentExchangeRate = response.rates[toCurrency] ?: 1.0
                    )
                )
                is ExchangeRatesDomainModel.Error -> notifyDialogCommand(response.message)
            }
        }
    }

    fun onBaseAmountChanged(baseAmount: Double) {
        val exchangeRate = currentViewState()?.currentExchangeRate ?: 1.0
        updateViewState(
            currentViewState()?.copy(
                toAmount = String.format("%.3f", baseAmount * exchangeRate).toDouble()
            )
        )
    }
}
