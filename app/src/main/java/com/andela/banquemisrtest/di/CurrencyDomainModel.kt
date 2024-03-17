package com.andela.banquemisrtest.di

import com.andela.domain.abstraction.usecase.UseCaseExecutor
import com.andela.domain.abstraction.usecase.UseCaseExecutorProvider
import com.andela.domain.currencyexchange.repository.CurrencyRepository
import com.andela.domain.currencyexchange.usecases.GetCurrenciesUseCase
import com.andela.domain.currencyexchange.usecases.GetCurrenciesUseCaseImpl
import com.andela.domain.currencyexchange.usecases.GetExchangeRatesUseCase
import com.andela.domain.currencyexchange.usecases.GetExchangeRatesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CurrencyDomainModel {

    @Provides
    fun providesGetCurrenciesUseCase(
        currencyRepository: CurrencyRepository
    ): GetCurrenciesUseCase = GetCurrenciesUseCaseImpl(
        currencyRepository
    )

    @Provides
    fun providesGetExchangeRatesUseCase(
        currencyRepository: CurrencyRepository
    ): GetExchangeRatesUseCase = GetExchangeRatesUseCaseImpl(
        currencyRepository
    )

    @Provides
    fun providesUseCaseExecutorProvider(): UseCaseExecutorProvider =
        { coroutineScope -> UseCaseExecutor(coroutineScope) }
}
