package com.andela.domain.currencyexchange.usecases

import com.andela.domain.currencyexchange.repository.CurrencyRepository

class GetCurrenciesUseCaseImpl(
    private val currencyRepository: CurrencyRepository
) : GetCurrenciesUseCase() {

    override suspend fun executeUseCase(request: Unit) = currencyRepository.fetchCurrencies()
}
