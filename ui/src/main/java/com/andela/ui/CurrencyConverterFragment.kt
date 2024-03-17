package com.andela.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.andela.presentation.CurrencyConverterViewModel
import com.andela.presentation.CurrencyConverterViewState
import com.andela.ui.databinding.FragmentCurrencyConverterBinding
import com.andela.ui.utilities.showAlertDialog
import com.andela.ui.utilities.swapSelectionWith
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
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) { viewModel.onFragmentViewCreated() }
        dataBinding.editTextBaseCurrency.requestFocus()
        dataBinding.btnSwap.setOnClickListener {
            dataBinding.spinnerBaseCurrency.swapSelectionWith(dataBinding.spinnerToCurrency)
            viewModel.onCurrenciesSwappedAction()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { savedState -> dataBinding.savedState = savedState }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BASE_SPINNER_STATE, dataBinding.spinnerBaseCurrency.selectedItemPosition)
        outState.putInt(TO_SPINNER_STATE, dataBinding.spinnerToCurrency.selectedItemPosition)
    }

    override fun updateViewState(viewState: CurrencyConverterViewState) {
        dataBinding.editTextToCurrency.setText(
            viewModel.onBaseAmountChangedAction(dataBinding.editTextBaseCurrency.text.toString())
        )
    }

    override fun notifyDialogCommand(message: String) {
        showAlertDialog(message = message)
    }
}
