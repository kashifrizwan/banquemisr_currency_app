package com.andela.ui.adapter

import android.content.Context
import android.widget.ArrayAdapter

class CurrenciesSpinnerAdapter(
    context: Context
) : ArrayAdapter<String>(
    context,
    android.R.layout.simple_spinner_dropdown_item,
    listOf<String>()
)
