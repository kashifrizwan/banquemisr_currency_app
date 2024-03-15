package com.andela.data.service.mapper

import com.andela.data.datasource.model.ExchangeRatesDataModel
import com.andela.data.service.model.ExchangeRateApiModel
import com.andela.domain.abstraction.exception.UnknownNetworkException

class ExchangeRatesApiToDataModelMapper {
    fun map(apiModel: ExchangeRateApiModel) = when {
        apiModel.success -> ExchangeRatesDataModel(rates = apiModel.rates)
        else -> throw UnknownNetworkException(apiModel.error.info)
    }
}
