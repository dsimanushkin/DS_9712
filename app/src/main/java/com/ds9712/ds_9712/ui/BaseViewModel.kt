package com.ds9712.ds_9712.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ds9712.ds_9712.util.DataChannelManager
import com.ds9712.ds_9712.util.DataState
import com.ds9712.ds_9712.util.StateEvent
import com.ds9712.ds_9712.util.StateMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewModel<ViewState>: ViewModel() {

    private val _viewState: MutableLiveData<ViewState> = MutableLiveData()

    private val dataChannelManager: DataChannelManager<ViewState> = object: DataChannelManager<ViewState>() {
        override fun handleNewData(data: ViewState) {
            this@BaseViewModel.handleNewData(data)
        }
    }

    val viewState: LiveData<ViewState> get() = _viewState

    val numActiveJobs: LiveData<Int> = dataChannelManager.numActiveJobs

    val stateMessage: LiveData<StateMessage?> get() = dataChannelManager.messageStack.stateMessage

    // For debugging
    fun getMessageStackSize(): Int {
        return dataChannelManager.messageStack.size
    }

    fun setupChannel() = dataChannelManager.setupChannel()

    abstract fun handleNewData(data: ViewState)

    abstract fun setStateEvent(stateEvent: StateEvent)

    fun launchJob(
        stateEvent: StateEvent,
        jobFunction: Flow<DataState<ViewState>>
    ) {
        dataChannelManager.launchJob(stateEvent, jobFunction)
    }

    fun areAnyJobsActive(): Boolean {
        return dataChannelManager.numActiveJobs.value?.let {
            it > 0
        }?: false
    }

    fun isJobAlreadyActive(stateEvent: StateEvent): Boolean {
        return dataChannelManager.isJobAlreadyActive(stateEvent)
    }

    fun getCurrentViewStateOrNew(): ViewState {
        return viewState.value ?: initNewViewState()
    }

    fun setViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    fun clearStateMessage(index: Int = 0) {
        dataChannelManager.clearStateMessage(index)
    }

    open fun cancelActiveJobs() {
        if (areAnyJobsActive()) {
            dataChannelManager.cancelJobs()
        }
    }

    abstract fun initNewViewState(): ViewState
}