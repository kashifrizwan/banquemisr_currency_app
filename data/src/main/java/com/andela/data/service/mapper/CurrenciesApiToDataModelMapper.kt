package com.andela.data.service.mapper

import com.andela.data.datasource.model.CurrenciesDataModel
import com.andela.data.service.model.CurrenciesApiModel
import com.andela.domain.abstraction.exception.UnknownNetworkException

class CurrenciesApiToDataModelMapper {
    fun map(apiModel: CurrenciesApiModel) = when {
        apiModel.success -> CurrenciesDataModel(apiModel.symbols)
        else -> throw UnknownNetworkException(apiModel.error.info)
    }
}
