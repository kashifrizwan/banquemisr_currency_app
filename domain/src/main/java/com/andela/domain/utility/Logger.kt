package com.andela.domain.utility

import android.util.Log

fun globalLogger(message: String?) {
    Log.d("Currency App", message ?: "Unknown Message!")
}
