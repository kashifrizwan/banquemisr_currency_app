package com.andela.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.andela.presentation.CurrencyConverterViewModel
import com.andela.presentation.CurrencyConverterViewState
import com.andela.ui.adapter.CurrenciesSpinnerAdapter
import com.andela.ui.utilities.CurrenciesTextWatcher
import com.andela.ui.utilities.showAlertDialog
import com.andela.ui.utilities.updateTextWatcher
import com.google.android.material.progressindicator.CircularProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val BASE_SPINNER_STATE = "BaseSpinnerState"
private const val TO_SPINNER_STATE = "ToSpinnerState"

@AndroidEntryPoint
class CurrencyConverterFragment : BaseFragment<CurrencyConverterViewState>(
    R.layout.fragment_currency_converter
) {
    override val viewModel: CurrencyConverterViewModel by viewModels()

    @Inject
    lateinit var currenciesAdapter: CurrenciesSpinnerAdapter

    private val baseCurrencySpinner: Spinner get() = requireView().findViewById(R.id.spinner_base_currency)
    private val toCurrencySpinner: Spinner get() = requireView().findViewById(R.id.spinner_to_currency)
    private val baseCurrencyEdittext: EditText get() = requireView().findViewById(R.id.editText_base_currency)
    private val toCurrencyEditText: EditText get() = requireView().findViewById(R.id.editText_to_currency)
    private val swapButton: Button get() = requireView().findViewById(R.id.btn_swap)
    private val progressBar: CircularProgressIndicator get() = requireView().findViewById(R.id.progress_bar)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) { viewModel.onFragmentViewCreated() }
        baseCurrencySpinner.adapter = currenciesAdapter
        toCurrencySpinner.adapter = currenciesAdapter
        setupClickListeners()
        setupOnItemChangeListeners()
        setupFocusChangeListeners()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { state ->
            baseCurrencySpinner.setSelection(state.getInt(BASE_SPINNER_STATE, 0))
            toCurrencySpinner.setSelection(state.getInt(TO_SPINNER_STATE, 0))
        }
    }

    override fun renderViewState(viewState: CurrencyConverterViewState) {
        progressBar.isVisible = viewState.isLoading
        updateCurrenciesList(viewState.availableCurrenciesList)
        onCurrencyValueChanged(baseCurrencyEdittext.text.toString(), toCurrencyEditText, isBaseAmount = true)
    }

    private fun updateCurrenciesList(currencies: List<String>) {
        currenciesAdapter.clear()
        currenciesAdapter.addAll(currencies)
        currenciesAdapter.notifyDataSetChanged()
    }

    override fun notifyDialogCommand(message: String) {
        showAlertDialog(message = message)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BASE_SPINNER_STATE, baseCurrencySpinner.selectedItemPosition)
        outState.putInt(TO_SPINNER_STATE, toCurrencySpinner.selectedItemPosition)
    }

    private fun setupClickListeners() {
        swapButton.setOnClickListener {
            val baseSpinnerSelectedPosition = baseCurrencySpinner.selectedItemPosition
            baseCurrencySpinner.setSelection(toCurrencySpinner.selectedItemPosition)
            toCurrencySpinner.setSelection(baseSpinnerSelectedPosition)
            viewModel.onCurrenciesSwappedAction()
        }
    }

    private fun setupOnItemChangeListeners() {
        baseCurrencySpinner.onItemSelectedListener = onCurrencySelectedListener
        toCurrencySpinner.onItemSelectedListener = onCurrencySelectedListener
    }

    private val onCurrencySelectedListener: OnItemSelectedListener by lazy {
        object: OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.onCurrencyChangedAction(
                    fromCurrency = baseCurrencySpinner.selectedItem.toString(),
                    toCurrency = toCurrencySpinner.selectedItem.toString()
                )
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupFocusChangeListeners() {
        baseCurrencyEdittext.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            baseCurrencyEdittext.updateTextWatcher(
                isActive = hasFocus,
                textWatcher= baseCurrencyTextChangeListener,
                adjacentInputView = toCurrencyEditText,
                adjacentTextWatcher = toCurrencyTextChangeListener
            )
        }

        toCurrencyEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            toCurrencyEditText.updateTextWatcher(
                isActive = hasFocus,
                textWatcher= toCurrencyTextChangeListener,
                adjacentInputView = baseCurrencyEdittext,
                adjacentTextWatcher = baseCurrencyTextChangeListener
            )
        }
    }

    private fun onCurrencyValueChanged(amount: String, editText: EditText, isBaseAmount: Boolean) {
        val calculatedAmount = viewModel.onInputAmountChangedAction(
            inputAmount = amount.toDoubleOrNull() ?: 1.0,
            isReverse = isBaseAmount.not()
        )
        editText.setText(calculatedAmount.toString())
    }

    private val baseCurrencyTextChangeListener: CurrenciesTextWatcher by lazy {
        CurrenciesTextWatcher(::onCurrencyValueChanged, toCurrencyEditText, isBaseAmount = true)
    }

    private val toCurrencyTextChangeListener: CurrenciesTextWatcher by lazy {
        CurrenciesTextWatcher(::onCurrencyValueChanged, baseCurrencyEdittext, isBaseAmount = false)
    }
}
