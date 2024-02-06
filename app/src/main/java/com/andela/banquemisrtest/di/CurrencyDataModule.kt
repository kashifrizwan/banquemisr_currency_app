package com.andela.banquemisrtest.di

import com.andela.data.datasource.CurrencyDataSource
import com.andela.data.datasource.CurrencySource
import com.andela.data.network.RetrofitBuilder
import com.andela.data.repository.CurrencyDataRepository
import com.andela.data.service.CurrencyService
import com.andela.domain.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CurrencyDataModule {

    @Provides
    fun providesRetrofitBuilder() = RetrofitBuilder()

    @Provides
    fun provideCurrencyService(retrofitBuilder: RetrofitBuilder): CurrencyService {
        return retrofitBuilder.retrofit.create(CurrencyService::class.java)
    }

    @Provides
    fun providesCurrencySource(
        currencyService: CurrencyService
    ): CurrencySource = CurrencyDataSource(currencyService)

    @Provides
    fun providesCurrencyRepository(
        currencySource: CurrencySource
    ): CurrencyRepository = CurrencyDataRepository(currencySource)
}
