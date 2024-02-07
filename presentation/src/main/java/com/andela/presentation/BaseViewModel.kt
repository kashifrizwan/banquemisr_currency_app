package com.andela.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<V> : ViewModel() {
    val viewState: MutableLiveData<V> = MutableLiveData()
    val dialogCommand: MutableLiveData<String> = MutableLiveData()

    abstract fun initialState(): V

    fun currentViewState(): V = viewState.value ?: initialState()

    protected fun updateViewState(updatedViewState: V) {
        viewState.postValue(updatedViewState ?: initialState())
    }

    protected fun notifyDialogCommand(message: String) {
        dialogCommand.postValue(message)
    }
}
