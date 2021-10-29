package com.ds9712.ds_9712.ui.main

import android.content.SharedPreferences
import com.ds9712.ds_9712.di.main.MainScope
import com.ds9712.ds_9712.repository.main.MainRepository
import com.ds9712.ds_9712.session.SessionManager
import com.ds9712.ds_9712.ui.BaseViewModel
import com.ds9712.ds_9712.ui.main.state.MainViewState
import com.ds9712.ds_9712.util.StateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@MainScope
class MainViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val mainRepository: MainRepository,
    sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor,
): BaseViewModel<MainViewState>() {
    override fun handleNewData(data: MainViewState) {
    }

    override fun setStateEvent(stateEvent: StateEvent) {
    }

    override fun initNewViewState(): MainViewState {
        return MainViewState()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}