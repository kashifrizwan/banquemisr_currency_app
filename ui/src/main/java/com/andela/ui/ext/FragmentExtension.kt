package com.andela.ui.ext

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
