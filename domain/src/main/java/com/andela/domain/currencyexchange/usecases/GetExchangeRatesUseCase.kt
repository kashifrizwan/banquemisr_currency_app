package com.andela.domain.currencyexchange.usecases

import com.andela.domain.abstraction.usecase.BaseUseCase
import com.andela.domain.currencyexchange.model.ExchangeRatesDomainModel
import com.andela.domain.currencyexchange.model.ExchangeRatesRequestDomainModel

abstract class GetExchangeRatesUseCase :
    BaseUseCase<ExchangeRatesRequestDomainModel, ExchangeRatesDomainModel>()
