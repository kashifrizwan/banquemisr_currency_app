package com.andela.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andela.domain.abstraction.usecase.UseCaseExecutor

abstract class BaseViewModel<V> : ViewModel() {

    val viewState: MutableLiveData<V> = MutableLiveData()
    val dialogCommand: MutableLiveData<String> = MutableLiveData()

    protected val useCaseExecutor: UseCaseExecutor by lazy { UseCaseExecutor(viewModelScope) }

    abstract fun initialState(): V

    fun currentViewState(): V = viewState.value ?: initialState()

    protected fun updateViewState(updatedViewState: V) {
        viewState.postValue(updatedViewState ?: initialState())
    }

    protected fun notifyDialogCommand(message: String) {
        dialogCommand.postValue(message)
    }
}
