package com.andela.banquemisrtest.di

import com.andela.domain.repository.CurrencyRepository
import com.andela.domain.usecases.GetCurrenciesUseCase
import com.andela.domain.usecases.GetCurrenciesUseCaseImpl
import com.andela.domain.usecases.GetExchangeRatesUseCase
import com.andela.domain.usecases.GetExchangeRatesUseCaseImpl
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
}
