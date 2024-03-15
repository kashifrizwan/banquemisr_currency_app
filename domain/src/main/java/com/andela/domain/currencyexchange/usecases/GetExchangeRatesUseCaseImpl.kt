package com.andela.domain.currencyexchange.usecases

import com.andela.domain.currencyexchange.model.ExchangeRatesRequestDomainModel
import com.andela.domain.currencyexchange.repository.CurrencyRepository

class GetExchangeRatesUseCaseImpl(
    private val currencyRepository: CurrencyRepository
) : GetExchangeRatesUseCase() {

    override suspend fun executeUseCase(request: ExchangeRatesRequestDomainModel) =
        currencyRepository.fetchExchangeRates(request.base, request.symbol)
}
