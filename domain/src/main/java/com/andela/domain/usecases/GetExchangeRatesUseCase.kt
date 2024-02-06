package com.andela.domain.usecases

import com.andela.domain.model.ExchangeRatesDomainModel
import com.andela.domain.model.ExchangeRatesRequestDomainModel

interface GetExchangeRatesUseCase {
    suspend fun execute(request: ExchangeRatesRequestDomainModel): ExchangeRatesDomainModel
}
