package com.andela.ui.utilities

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CurrenciesTextWatcher (
    private val onCurrencyValueChanged: (amount: String, editText: EditText, isBaseAmount: Boolean) -> Unit,
    private val editText: EditText,
    private val isBaseAmount: Boolean
) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(text: Editable?) {
        onCurrencyValueChanged(text.toString(), editText, isBaseAmount)
    }
}
