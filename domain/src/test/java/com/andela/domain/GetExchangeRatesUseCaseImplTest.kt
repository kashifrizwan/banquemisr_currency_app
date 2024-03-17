package com.andela.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andela.domain.abstraction.exception.UnknownNetworkException
import com.andela.domain.currencyexchange.model.ExchangeRatesDomainModel
import com.andela.domain.currencyexchange.model.ExchangeRatesRequestDomainModel
import com.andela.domain.currencyexchange.repository.CurrencyRepository
import com.andela.domain.currencyexchange.usecases.GetExchangeRatesUseCaseImpl
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.given
import kotlin.test.assertFails

@RunWith(MockitoJUnitRunner::class)
class GetExchangeRatesUseCaseImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var classUnderTest: GetExchangeRatesUseCaseImpl

    @Mock
    private lateinit var currencyRepository: CurrencyRepository

    @Before
    fun setup() {
        classUnderTest = GetExchangeRatesUseCaseImpl(
            currencyRepository = currencyRepository
        )
    }

    @Test
    fun `Given successful execution of GetExchangeRatesUseCaseImpl Then return the Exchange Rates`() = runTest {
        // Given
        val expectedResult = ExchangeRatesDomainModel(
            rates = hashMapOf(
                Pair("AED", 2.55),
                Pair("AFN", 2.55),
                Pair("AMD", 4.45),
                Pair("PKR", 122.6)
            )
        )
        given(currencyRepository.fetchExchangeRates("AED", "PKR")).willReturn(expectedResult)

        // When
        val actualResult = classUnderTest.executeUseCase(ExchangeRatesRequestDomainModel("AED", "PKR"))

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given failure execution of GetExchangeRatesUseCaseImpl Then return the UnknownNetworkException`() = runTest {
        // Given
        given(currencyRepository.fetchExchangeRates("AED", "PKR")).willAnswer{ throw UnknownNetworkException() }

        // Then
        assertFails {
            // When
            classUnderTest.executeUseCase(ExchangeRatesRequestDomainModel("AED", "PKR"))
        }
    }
}
