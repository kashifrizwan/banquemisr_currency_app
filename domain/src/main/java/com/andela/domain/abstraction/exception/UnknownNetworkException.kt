package com.andela.domain.abstraction.exception

class UnknownNetworkException(val errorMessage: String = "Unknown Error Occurred!") : Exception(errorMessage)
