package com.andela.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<V> : ViewModel() {
    val viewState: MutableLiveData<V?> = MutableLiveData()
    val dialogCommand: MutableLiveData<String> = MutableLiveData()

    protected fun currentViewState() = viewState.value

    protected fun updateViewState(updatedViewState: V?) {
        viewState.postValue(updatedViewState)
    }

    protected fun notifyDialogCommand(message: String) {
        dialogCommand.postValue(message)
    }
}
