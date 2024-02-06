package com.andela.domain.usecases

import com.andela.domain.model.CurrenciesDomainModel

interface GetCurrenciesUseCase {
    suspend fun execute(): CurrenciesDomainModel
}
