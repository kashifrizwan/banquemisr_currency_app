package com.andela.ui.utilities

import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

fun Fragment.showAlertDialog(
    title: String = "Something went wrong!",
    message: String = "Please try again later."
) {
    val builder = AlertDialog.Builder(requireContext())
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(android.R.string.ok) { _, _ -> }
    builder.show()
}

fun EditText.updateTextWatcher(
    isActive: Boolean,
    textWatcher: TextWatcher,
    adjacentInputView: EditText,
    adjacentTextWatcher: TextWatcher
) {
    if (isActive) {
        this.addTextChangedListener(textWatcher)
        adjacentInputView.removeTextChangedListener(adjacentTextWatcher)
    } else {
        this.removeTextChangedListener(textWatcher)
        adjacentInputView.addTextChangedListener(adjacentTextWatcher)
    }
}
