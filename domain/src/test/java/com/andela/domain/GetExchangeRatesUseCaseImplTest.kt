package com.andela.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andela.domain.model.ExchangeRatesDomainModel.Error
import com.andela.domain.model.ExchangeRatesDomainModel.ExchangeRatesSuccess
import com.andela.domain.model.ExchangeRatesRequestDomainModel
import com.andela.domain.repository.CurrencyRepository
import com.andela.domain.usecases.GetExchangeRatesUseCaseImpl
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.given

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
        val expectedResult = ExchangeRatesSuccess(
            rates = hashMapOf(
                Pair("AED", 2.55),
                Pair("AFN", 2.55),
                Pair("AMD", 4.45),
                Pair("PKR", 122.6)
            )
        )
        given(currencyRepository.fetchExchangeRates("AED", "PKR")).willReturn(expectedResult)

        // When
        val actualResult = classUnderTest.execute(ExchangeRatesRequestDomainModel("AED", "PKR"))

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given error execution of GetExchangeRatesUseCaseImpl Then return the error`() = runTest {
        // Given
        val expectedResult = Error("Unexpected Error!")
        given(currencyRepository.fetchExchangeRates("AED", "PKR")).willReturn(expectedResult)

        // When
        val actualResult = classUnderTest.execute(ExchangeRatesRequestDomainModel("AED", "PKR"))

        // Then
        assertEquals(expectedResult, actualResult)
    }
}
