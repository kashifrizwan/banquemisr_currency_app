package com.andela.domain.usecases

import com.andela.domain.model.ExchangeRatesRequestDomainModel
import com.andela.domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetExchangeRatesUseCaseImpl(
    private val currencyRepository: CurrencyRepository
) : GetExchangeRatesUseCase {
    override suspend fun execute(
        request: ExchangeRatesRequestDomainModel
    ) = withContext(Dispatchers.Default) {
        currencyRepository.fetchExchangeRates(request.base, request.symbol)
    }
}
