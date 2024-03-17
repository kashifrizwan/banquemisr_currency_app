package com.andela.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andela.domain.abstraction.exception.UnknownNetworkException
import com.andela.domain.currencyexchange.model.CurrenciesDomainModel
import com.andela.domain.currencyexchange.repository.CurrencyRepository
import com.andela.domain.currencyexchange.usecases.GetCurrenciesUseCaseImpl
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
class GetCurrenciesUseCaseImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var classUnderTest: GetCurrenciesUseCaseImpl

    @Mock
    private lateinit var currencyRepository: CurrencyRepository

    @Before
    fun setup() {
        classUnderTest = GetCurrenciesUseCaseImpl(
            currencyRepository = currencyRepository
        )
    }

    @Test
    fun `Given successful execution of GetCurrenciesUseCase Then return the Currencies`() = runTest {
        // Given
        val expectedResult = CurrenciesDomainModel(
            currencies = hashMapOf(
                Pair("AED", "United Arab Emirates Dirham"),
                Pair("AFN", "Afghan Afghani"),
                Pair("PKR", "Pakistani Rupee"),
                Pair("AMD", "Armenian Dram")
            )
        )
        given(currencyRepository.fetchCurrencies()).willReturn(expectedResult)

        // When
        val actualResult = classUnderTest.executeUseCase(Unit)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given failed execution of GetCurrenciesUseCase Then return the UnknownNetworkException`() = runTest {
        // Given
        given(currencyRepository.fetchCurrencies()).willAnswer{ throw UnknownNetworkException() }

        // Then
        assertFails {
            // When
            classUnderTest.executeUseCase(Unit)
        }
    }
}
