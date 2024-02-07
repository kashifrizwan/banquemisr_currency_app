package com.andela.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andela.domain.model.CurrenciesDomainModel.Currencies
import com.andela.domain.model.CurrenciesDomainModel.Error
import com.andela.domain.model.ExchangeRatesDomainModel
import com.andela.domain.model.ExchangeRatesDomainModel.ExchangeRatesSuccess
import com.andela.domain.model.ExchangeRatesRequestDomainModel
import com.andela.domain.usecases.GetCurrenciesUseCase
import com.andela.domain.usecases.GetExchangeRatesUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.given

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

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        classUnderTest = CurrencyConverterViewModel(
            getCurrenciesUseCase = getCurrenciesUseCase,
            getExchangeRatesUseCase = getExchangeRatesUseCase
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
        val givenCurrencies = Currencies(currencies = hashMapOf(
            Pair("AED", "United Arab Emirates Dirham"),
            Pair("AFN", "Afghan Afghani"),
            Pair("PKR", "Pakistani Rupee"),
            Pair("AMD", "Armenian Dram")
        ))
        runBlocking { given(getCurrenciesUseCase.execute()).willReturn(givenCurrencies) }

        // When
        classUnderTest.onFragmentCreated()
        val actualResult = classUnderTest.currentViewState().availableCurrenciesList

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given Error execution of GetCurrenciesUseCase Then update the DialogCommand`() {
        // Given
        val expectedResult = "Unexpected network error!"
        val givenError = Error(message = "Unexpected network error!")
        runBlocking { given(getCurrenciesUseCase.execute()).willReturn(givenError) }

        // When
        classUnderTest.onFragmentCreated()
        val actualResult = classUnderTest.dialogCommand.value

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given successful execution of GetExchangeRatesUseCase Then update the Exchange Rate in ViewState`() {
        // Given
        val expectedResult = 122.6
        val givenExchangeRates = ExchangeRatesSuccess(rates = hashMapOf(
            Pair("AED", 2.55),
            Pair("AFN", 2.55),
            Pair("AMD", 4.45),
            Pair("PKR", 122.6)
        ))
        val givenExchangeRateRequestModel = ExchangeRatesRequestDomainModel(base = "AED", symbol = "PKR")
        runBlocking { given(getExchangeRatesUseCase.execute(givenExchangeRateRequestModel)).willReturn(givenExchangeRates) }

        // When
        classUnderTest.onCurrencyChangedAction(fromCurrency = "AED", toCurrency = "PKR")
        val actualResult = classUnderTest.currentViewState().exchangeRateForSelectedCurrencies

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given Error execution of GetExchangeRatesUseCase Then update the DialogCommand`() {
        // Given
        val expectedResult = "Unable to retrieve exchange rates!"
        val givenExchangeRates = ExchangeRatesDomainModel.Error(message = "Unable to retrieve exchange rates!")
        val givenExchangeRateRequestModel = ExchangeRatesRequestDomainModel(base = "AED", symbol = "PKR")
        runBlocking { given(getExchangeRatesUseCase.execute(givenExchangeRateRequestModel)).willReturn(givenExchangeRates) }

        // When
        classUnderTest.onCurrencyChangedAction(fromCurrency = "AED", toCurrency = "PKR")
        val actualResult = classUnderTest.dialogCommand.value

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given successful execution of onInputAmountChangedAction When is not reverse Then return formatted amount`() {
        // Given
        val expectedResult = 310.8
        classUnderTest.viewState.postValue(CurrencyConverterViewState(exchangeRateForSelectedCurrencies = 55.5))

        // When
        val actualResult = classUnderTest.onInputAmountChangedAction(inputAmount = 5.6, isReverse = false)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given successful execution of onInputAmountChangedAction When is reverse Then return formatted amount`() {
        // Given
        val expectedResult = 0.101
        classUnderTest.viewState.postValue(CurrencyConverterViewState(exchangeRateForSelectedCurrencies = 55.5))

        // When
        val actualResult = classUnderTest.onInputAmountChangedAction(inputAmount = 5.6, isReverse = true)

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
