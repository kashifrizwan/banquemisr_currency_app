package com.andela.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andela.data.datasource.CurrencySource
import com.andela.data.datasource.model.CurrenciesDataModel
import com.andela.data.datasource.model.CurrenciesDataModel.Currencies
import com.andela.data.datasource.model.ExchangeRatesDataModel
import com.andela.data.repository.CurrencyDataRepository
import com.andela.domain.model.CurrenciesDomainModel
import com.andela.domain.model.ExchangeRatesDomainModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.given

private val givenCurrenciesMap = hashMapOf(
    Pair("AED", "United Arab Emirates Dirham"),
    Pair("AFN", "Afghan Afghani"),
    Pair("PKR", "Pakistani Rupee"),
    Pair("AMD", "Armenian Dram")
)

private val givenExchangeRatesMap = hashMapOf(
    Pair("AED", 2.55),
    Pair("AFN", 2.55),
    Pair("AMD", 4.45),
    Pair("PKR", 122.6)
)

@RunWith(MockitoJUnitRunner::class)
class CurrencyDataRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var classUnderTest: CurrencyDataRepository

    @Mock
    private lateinit var dataSource: CurrencySource

    @Before
    fun setup() {
        classUnderTest = CurrencyDataRepository(
            dataSource = dataSource
        )
    }

    @Test
    fun `Given successful execution of fetchCurrencies Then return the Currencies`() = runTest {
        // Given
        val givenCurrencies = Currencies(currencies = givenCurrenciesMap)
        val expectedResult = CurrenciesDomainModel.Currencies(currencies = givenCurrenciesMap)
        given(dataSource.fetchCurrencies()).willReturn(givenCurrencies)

        // When
        val actualResult = classUnderTest.fetchCurrencies()

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given api error in execution of fetchCurrencies Then return error`() = runTest {
        // Given
        val givenInput = CurrenciesDataModel.ApiError(message = "API key is not valid!")
        val expectedResult = CurrenciesDomainModel.Error(message = "API key is not valid!")
        given(dataSource.fetchCurrencies()).willReturn(givenInput)

        // When
        val actualResult = classUnderTest.fetchCurrencies()

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given successful execution of fetchExchangeRates Then return the Exchange rates`() = runTest {
        // Given
        val givenInput = ExchangeRatesDataModel.ExchangeRates(rates = givenExchangeRatesMap)
        val expectedResult = ExchangeRatesDomainModel.ExchangeRatesSuccess(rates = givenExchangeRatesMap)
        given(dataSource.fetchExchangeRate("AED", "PKR")).willReturn(givenInput)

        // When
        val actualResult = classUnderTest.fetchExchangeRates("AED", "PKR")

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given api error in execution of fetchExchangeRates Then return error`() = runTest {
        // Given
        val givenInput = ExchangeRatesDataModel.ApiError(message = "API key is not valid!")
        val expectedResult = ExchangeRatesDomainModel.Error(message = "API key is not valid!")
        given(dataSource.fetchExchangeRate("AED", "PKR")).willReturn(givenInput)

        // When
        val actualResult = classUnderTest.fetchExchangeRates("AED", "PKR")

        // Then
        assertEquals(expectedResult, actualResult)
    }
}
