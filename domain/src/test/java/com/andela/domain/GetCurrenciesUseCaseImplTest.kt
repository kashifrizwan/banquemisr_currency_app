package com.andela.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andela.domain.model.CurrenciesDomainModel.Error
import com.andela.domain.model.CurrenciesDomainModel.Currencies
import com.andela.domain.repository.CurrencyRepository
import com.andela.domain.usecases.GetCurrenciesUseCaseImpl
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
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
class GetCurrenciesUseCaseImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var classUnderTest: GetCurrenciesUseCaseImpl

    @Mock
    private lateinit var currencyRepository: CurrencyRepository

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        classUnderTest = GetCurrenciesUseCaseImpl(
            currencyRepository = currencyRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Given successful execution of GetCurrenciesUseCase Then return the Currencies`() = runTest {
        // Given
        val expectedResult = Currencies(
            currencies = hashMapOf(
                Pair("AED", "United Arab Emirates Dirham"),
                Pair("AFN", "Afghan Afghani"),
                Pair("PKR", "Pakistani Rupee"),
                Pair("AMD", "Armenian Dram")
            )
        )
        runBlocking { given(currencyRepository.fetchCurrencies()).willReturn(expectedResult) }

        // When
        val actualResult = classUnderTest.execute()

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given error execution of GetCurrenciesUseCase Then return the error`() = runTest {
        // Given
        val expectedResult = Error("Unexpected Error!")
        runBlocking { given(currencyRepository.fetchCurrencies()).willReturn(expectedResult) }

        // When
        val actualResult = classUnderTest.execute()

        // Then
        assertEquals(expectedResult, actualResult)
    }
}
