package com.andela.domain.currencyexchange.usecases

import com.andela.domain.abstraction.usecase.BaseUseCase
import com.andela.domain.currencyexchange.model.CurrenciesDomainModel

abstract class GetCurrenciesUseCase : BaseUseCase<Unit, CurrenciesDomainModel>()
