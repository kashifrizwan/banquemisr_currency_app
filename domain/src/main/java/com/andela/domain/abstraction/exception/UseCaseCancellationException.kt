package com.andela.domain.abstraction.exception

class UseCaseCancellationException(val errorMessage: String = "Unknown Error Occurred!") : Exception(errorMessage)
