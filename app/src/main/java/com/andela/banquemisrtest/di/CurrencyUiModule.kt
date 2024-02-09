package com.andela.banquemisrtest.di

import android.content.Context
import com.andela.ui.adapter.CurrenciesSpinnerAdapter
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CurrencyUiModule {

    @Provides
    @Reusable
    fun providesCurrenciesSpinnerAdapter(
        @ApplicationContext context: Context
    ) = CurrenciesSpinnerAdapter(context)
}
