package com.andela.presentation

import com.andela.domain.abstraction.usecase.UseCaseExecutorProvider
import com.andela.domain.currencyexchange.model.ExchangeRatesRequestDomainModel
import com.andela.domain.currencyexchange.usecases.GetCurrenciesUseCase
import com.andela.domain.currencyexchange.usecases.GetExchangeRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase,
    useCaseExecutorProvider: UseCaseExecutorProvider
) : BaseViewModel<CurrencyConverterViewState>(useCaseExecutorProvider) {

    override fun initialState() = CurrencyConverterViewState()

    private var getCurrenciesJob: Job? = null
    private var getExchangeRatesJob: Job? = null

    fun onFragmentViewCreated() {
        updateProgressBarVisibility(isVisible = true)
        if (getCurrenciesJob?.isActive == true) { getCurrenciesJob?.cancel() }
        getCurrenciesJob = useCaseExecutor.execute(
            useCase = getCurrenciesUseCase,
            callback = { result -> updateViewState(
                currentViewState().copy(
                    isLoading = false,
                    currenciesList = result.currencies.keys.toList().sorted()
                )
            )},
            onError = { errorMessage ->
                updateProgressBarVisibility(isVisible = false)
                notifyDialogCommand(errorMessage)
            }
        )
    }

    fun onCurrencyChangedAction(fromCurrency: Int = 0, toCurrency: Int = 0) {
        if (fromCurrency == toCurrency) return
        updateProgressBarVisibility(isVisible = true)
        if (getExchangeRatesJob?.isActive == true) { getExchangeRatesJob?.cancel() }
        getExchangeRatesJob = useCaseExecutor.execute(
            useCase = getExchangeRatesUseCase,
            request = ExchangeRatesRequestDomainModel(
                currentViewState().currenciesList[fromCurrency],
                currentViewState().currenciesList[toCurrency]
            ),
            callback = { exchangeRates -> updateViewState(
                currentViewState().copy(
                    isLoading = false,
                    exchangeRateForSelectedCurrencies = exchangeRates.rates[currentViewState().currenciesList[toCurrency]] ?: 1.0
                )
            )},
            onError = { errorMessage ->
                updateProgressBarVisibility(isVisible = false)
                notifyDialogCommand(errorMessage)
            }
        )
    }

    fun onBaseAmountChangedAction(baseAmount: String) =
        String.format("%.1f", (baseAmount.toDoubleOrNull() ?: 1.0) * currentViewState().exchangeRateForSelectedCurrencies)

    fun onTargetAmountChangedAction(targetAmount: String) =
        String.format("%.1f", (targetAmount.toDoubleOrNull() ?: 1.0) / currentViewState().exchangeRateForSelectedCurrencies)

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
