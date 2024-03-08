package com.andela.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.andela.presentation.CurrencyConverterViewModel
import com.andela.presentation.CurrencyConverterViewState
import com.andela.ui.databinding.FragmentCurrencyConverterBinding
import com.andela.ui.utilities.CurrenciesTextWatcher
import com.andela.ui.utilities.showAlertDialog
import com.andela.ui.utilities.updateTextWatcher
import dagger.hilt.android.AndroidEntryPoint

private const val BASE_SPINNER_STATE = "BaseSpinnerState"
private const val TO_SPINNER_STATE = "ToSpinnerState"

@AndroidEntryPoint
class CurrencyConverterFragment : BaseFragment<CurrencyConverterViewState>() {

    override val viewModel: CurrencyConverterViewModel by viewModels()

    private var _dataBinding: FragmentCurrencyConverterBinding? = null

    private val dataBinding get() = _dataBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _dataBinding = FragmentCurrencyConverterBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel
        dataBinding.spinnerBaseCurrency.getSelectedItemPosition()
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) { viewModel.onFragmentViewCreated() }
        setupClickListeners()
        setupFocusChangeListeners()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { savedState -> dataBinding.savedState = savedState }
    }

    override fun renderViewState(viewState: CurrencyConverterViewState) {
        onCurrencyValueChanged(
            dataBinding.editTextBaseCurrency.text.toString(),
            dataBinding.editTextToCurrency,
            isBaseAmount = true
        )
    }

    override fun notifyDialogCommand(message: String) {
        showAlertDialog(message = message)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BASE_SPINNER_STATE, dataBinding.spinnerBaseCurrency.selectedItemPosition)
        outState.putInt(TO_SPINNER_STATE, dataBinding.spinnerToCurrency.selectedItemPosition)
    }

    private fun setupClickListeners() {
        dataBinding.btnSwap.setOnClickListener {
            val baseSpinnerSelectedPosition = dataBinding.spinnerBaseCurrency.selectedItemPosition
            dataBinding.spinnerBaseCurrency.setSelection(dataBinding.spinnerToCurrency.selectedItemPosition)
            dataBinding.spinnerToCurrency.setSelection(baseSpinnerSelectedPosition)
            viewModel.onCurrenciesSwappedAction()
        }
    }

    private fun setupFocusChangeListeners() {
        dataBinding.editTextBaseCurrency.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            dataBinding.editTextBaseCurrency.updateTextWatcher(
                isActive = hasFocus,
                textWatcher= baseCurrencyTextChangeListener,
                adjacentInputView = dataBinding.editTextToCurrency,
                adjacentTextWatcher = toCurrencyTextChangeListener
            )
        }

        dataBinding.editTextToCurrency.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            dataBinding.editTextToCurrency.updateTextWatcher(
                isActive = hasFocus,
                textWatcher= toCurrencyTextChangeListener,
                adjacentInputView = dataBinding.editTextBaseCurrency,
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
        CurrenciesTextWatcher(::onCurrencyValueChanged, dataBinding.editTextToCurrency, isBaseAmount = true)
    }

    private val toCurrencyTextChangeListener: CurrenciesTextWatcher by lazy {
        CurrenciesTextWatcher(::onCurrencyValueChanged, dataBinding.editTextBaseCurrency, isBaseAmount = false)
    }
}
