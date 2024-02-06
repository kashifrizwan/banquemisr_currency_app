package com.andela.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.andela.presentation.BaseViewModel

abstract class BaseFragment<V>(
    @LayoutRes val layoutId: Int
) : Fragment(layoutId) {

    abstract val viewModel: BaseViewModel<V>

    protected abstract fun renderViewState(viewState: V?)
    protected abstract fun notifyDialogCommand(message: String)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            renderViewState(viewState)
        }

        viewModel.dialogCommand.observe(viewLifecycleOwner) { message ->
            notifyDialogCommand(message)
        }
    }
}
