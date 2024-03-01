package com.andela.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.andela.presentation.CurrencyConverterViewModel
import com.andela.presentation.CurrencyConverterViewState
import com.andela.ui.adapter.CurrenciesSpinnerAdapter
import com.andela.ui.databinding.FragmentCurrencyConverterBinding
import com.andela.ui.utilities.CurrenciesTextWatcher
import com.andela.ui.utilities.showAlertDialog
import com.andela.ui.utilities.updateTextWatcher
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val BASE_SPINNER_STATE = "BaseSpinnerState"
private const val TO_SPINNER_STATE = "ToSpinnerState"

@AndroidEntryPoint
class CurrencyConverterFragment : BaseFragment<CurrencyConverterViewState>() {

    override val viewModel: CurrencyConverterViewModel by viewModels()

    @Inject
    lateinit var currenciesAdapter: CurrenciesSpinnerAdapter

    private var _dataBinding: FragmentCurrencyConverterBinding? = null

    private val dataBinding get() = _dataBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _dataBinding = FragmentCurrencyConverterBinding.inflate(inflater, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) { viewModel.onFragmentViewCreated() }
        dataBinding.spinnerBaseCurrency.adapter = currenciesAdapter
        dataBinding.spinnerToCurrency.adapter = currenciesAdapter
        setupClickListeners()
        setupOnItemChangeListeners()
        setupFocusChangeListeners()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { state ->
            dataBinding.spinnerBaseCurrency.setSelection(state.getInt(BASE_SPINNER_STATE, 0))
            dataBinding.spinnerToCurrency.setSelection(state.getInt(TO_SPINNER_STATE, 0))
        }
    }

    override fun renderViewState(viewState: CurrencyConverterViewState) {
        dataBinding.progressBar.isVisible = viewState.isLoading
        updateCurrenciesList(viewState.availableCurrenciesList)
        onCurrencyValueChanged(
            dataBinding.editTextBaseCurrency.text.toString(),
            dataBinding.editTextToCurrency,
            isBaseAmount = true
        )
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

    private fun setupOnItemChangeListeners() {
        dataBinding.spinnerBaseCurrency.onItemSelectedListener = onCurrencySelectedListener
        dataBinding.spinnerToCurrency.onItemSelectedListener = onCurrencySelectedListener
    }

    private val onCurrencySelectedListener: OnItemSelectedListener by lazy {
        object: OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.onCurrencyChangedAction(
                    fromCurrency = dataBinding.spinnerBaseCurrency.selectedItem.toString(),
                    toCurrency = dataBinding.spinnerToCurrency.selectedItem.toString()
                )
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
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
