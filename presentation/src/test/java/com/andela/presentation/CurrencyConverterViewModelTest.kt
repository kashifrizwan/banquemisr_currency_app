package com.andela.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andela.domain.abstraction.usecase.UseCaseExecutor
import com.andela.domain.currencyexchange.model.CurrenciesDomainModel
import com.andela.domain.currencyexchange.model.ExchangeRatesDomainModel
import com.andela.domain.currencyexchange.model.ExchangeRatesRequestDomainModel
import com.andela.domain.currencyexchange.usecases.GetCurrenciesUseCase
import com.andela.domain.currencyexchange.usecases.GetExchangeRatesUseCase
import com.andela.presentation.extensions.givenFailedUseCaseExecution
import com.andela.presentation.extensions.givenSuccessfulUseCaseExecution
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class CurrencyConverterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var classUnderTest: CurrencyConverterViewModel

    @Mock
    private lateinit var getCurrenciesUseCase: GetCurrenciesUseCase

    @Mock
    private lateinit var getExchangeRatesUseCase: GetExchangeRatesUseCase

    @Mock
    private lateinit var useCaseExecutor: UseCaseExecutor

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        classUnderTest = CurrencyConverterViewModel(
            getCurrenciesUseCase = getCurrenciesUseCase,
            getExchangeRatesUseCase = getExchangeRatesUseCase,
            useCaseExecutorProvider = { useCaseExecutor }
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Given successful execution of GetCurrenciesUseCase Then update the Currencies List in ViewState`() {
        // Given
        val expectedResult = listOf("AED", "AFN", "AMD", "PKR")
        val givenCurrencies = CurrenciesDomainModel(currencies = hashMapOf(
            Pair("AED", "United Arab Emirates Dirham"),
            Pair("AFN", "Afghan Afghani"),
            Pair("PKR", "Pakistani Rupee"),
            Pair("AMD", "Armenian Dram")
        ))
        useCaseExecutor.givenSuccessfulUseCaseExecution(
            useCase = getCurrenciesUseCase,
            result = givenCurrencies
        )

        // When
        classUnderTest.onFragmentViewCreated()
        val actualResult = classUnderTest.currentViewState().currenciesList

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given Failed execution of GetCurrenciesUseCase Then update the DialogCommand`() {
        // Given
        val expectedResult = "Unexpected network error!"
        useCaseExecutor.givenFailedUseCaseExecution(
            useCase = getCurrenciesUseCase,
            error = "Unexpected network error!"
        )

        // When
        classUnderTest.onFragmentViewCreated()
        val actualResult = classUnderTest.dialogCommand.value

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given successful execution of GetExchangeRatesUseCase Then update the Exchange Rate in ViewState`() {
        // Given
        val expectedResult = 122.6
        val givenExchangeRates = ExchangeRatesDomainModel(rates = hashMapOf(
            Pair("AED", 2.55),
            Pair("AFN", 2.55),
            Pair("AMD", 4.45),
            Pair("PKR", 122.6)
        ))
        val givenExchangeRateRequestModel = ExchangeRatesRequestDomainModel(base = "AED", symbol = "PKR")
        classUnderTest.viewState.postValue(CurrencyConverterViewState(currenciesList = listOf("AED", "AFN", "AMD", "PKR")))
        useCaseExecutor.givenSuccessfulUseCaseExecution(
            useCase = getExchangeRatesUseCase,
            request = givenExchangeRateRequestModel,
            result = givenExchangeRates
        )

        // When
        classUnderTest.onCurrencyChangedAction(fromCurrency = 0, toCurrency = 3)
        val actualResult = classUnderTest.currentViewState().exchangeRateForSelectedCurrencies

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given Failed execution of GetExchangeRatesUseCase Then update the DialogCommand`() {
        // Given
        val expectedResult = "Unable to retrieve exchange rates!"
        val givenExchangeRateRequestModel = ExchangeRatesRequestDomainModel(base = "AED", symbol = "PKR")
        classUnderTest.viewState.postValue(CurrencyConverterViewState(currenciesList = listOf("AED", "AFN", "AMD", "PKR")))
        useCaseExecutor.givenFailedUseCaseExecution(
            useCase = getExchangeRatesUseCase,
            request = givenExchangeRateRequestModel,
            error = "Unable to retrieve exchange rates!"
        )

        // When
        classUnderTest.onCurrencyChangedAction(fromCurrency = 0, toCurrency = 3)
        val actualResult = classUnderTest.dialogCommand.value

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given successful execution of onBaseAmountChangedAction Then return formatted calculated amount`() {
        // Given
        val expectedResult = "310.8"
        classUnderTest.viewState.postValue(CurrencyConverterViewState(exchangeRateForSelectedCurrencies = 55.5))

        // When
        val actualResult = classUnderTest.onBaseAmountChangedAction(baseAmount = "5.6")

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given successful execution of onTargetAmountChangedAction Then return formatted calculated amount`() {
        // Given
        val expectedResult = "0.1"
        classUnderTest.viewState.postValue(CurrencyConverterViewState(exchangeRateForSelectedCurrencies = 55.5))

        // When
        val actualResult = classUnderTest.onTargetAmountChangedAction(targetAmount = "5.6")

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given successful execution of onCurrenciesSwappedAction Then return swapped exchange rate`() {
        // Given
        val expectedResult = 0.018
        classUnderTest.viewState.postValue(CurrencyConverterViewState(exchangeRateForSelectedCurrencies = 55.5))

        // When
        classUnderTest.onCurrenciesSwappedAction()
        val exchangeRate = classUnderTest.currentViewState().exchangeRateForSelectedCurrencies
        val actualResult = String.format("%.3f", exchangeRate).toDouble()

            // Then
        assertEquals(expectedResult, actualResult)
    }
}
