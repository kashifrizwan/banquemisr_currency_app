package com.andela.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.viewModels
import com.andela.presentation.CurrencyConverterViewModel
import com.andela.presentation.CurrencyConverterViewState
import com.andela.ui.adapter.CurrenciesSpinnerAdapter
import com.andela.ui.ext.showAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyConverterFragment : BaseFragment<CurrencyConverterViewState>(
    R.layout.fragment_currency_converter
) {
    override val viewModel: CurrencyConverterViewModel by viewModels()

    //@Inject lateinit var currenciesAdapter: CurrenciesSpinnerAdapter

    private val baseCurrencySpinner: Spinner get() = requireView().findViewById(R.id.spinner_base_currency)
    private val toCurrencySpinner: Spinner get() = requireView().findViewById(R.id.spinner_to_currency)
    private val baseCurrencyEdittext: EditText get() = requireView().findViewById(R.id.editText_base_currency)
    private val toCurrencyEditText: EditText get() = requireView().findViewById(R.id.editText_to_currency)
    private val swapButton: Button get() = requireView().findViewById(R.id.btn_swap)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onFragmentCreated()
        /*baseCurrencySpinner.adapter = currenciesAdapter
        toCurrencySpinner.adapter = currenciesAdapter*/
    }

    override fun renderViewState(viewState: CurrencyConverterViewState?) {
        /*currenciesAdapter.addAll(viewState?.availableCurrenciesList ?: listOf())
        currenciesAdapter.notifyDataSetChanged()*/
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, viewState?.availableCurrenciesList ?: listOf())
        baseCurrencySpinner.adapter = adapter
        toCurrencySpinner.adapter = adapter

        toCurrencyEditText.setText(viewState?.toAmount.toString())
    }

    override fun notifyDialogCommand(message: String) {
        showAlertDialog(message = message)
    }

    private fun setupClickListeners() {
        swapButton.setOnClickListener {
            val baseSpinnerSelectedPosition = baseCurrencySpinner.selectedItemPosition
            baseCurrencySpinner.setSelection(toCurrencySpinner.selectedItemPosition)
            toCurrencySpinner.setSelection(baseSpinnerSelectedPosition)
        }
    }

    private fun setupOnItemChangeListeners() {
        baseCurrencySpinner.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onCurrencyChanged()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        toCurrencySpinner.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onCurrencyChanged()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupOnTextChangeListeners() {
        baseCurrencyEdittext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                /*viewModel.onBaseAmountChanged(
                    baseAmount = binding.editTextBaseCurrency.text.toString().toDoubleOrNull() ?: 0.0
                )*/
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun onCurrencyChanged() {
        viewModel.onCurrencyChangeAction(
            fromCurrency = baseCurrencySpinner.selectedItem.toString(),
            toCurrency = toCurrencySpinner.selectedItem.toString()
        )
    }
}
