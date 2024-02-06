package com.andela.domain.usecases

import com.andela.domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCurrenciesUseCaseImpl(
    private val currencyRepository: CurrencyRepository
) : GetCurrenciesUseCase {
    override suspend fun execute() = withContext(Dispatchers.Default) {
        currencyRepository.fetchCurrencies()
    }
}
