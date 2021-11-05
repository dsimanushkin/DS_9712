package com.ds9712.ds_9712.repository.main

import com.ds9712.ds_9712.di.main.MainScope
import com.ds9712.ds_9712.models.AuthToken
import com.ds9712.ds_9712.ui.main.state.MainViewState
import com.ds9712.ds_9712.util.DataState
import com.ds9712.ds_9712.util.StateEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@MainScope
interface MainRepository {

    fun getCurrentQuestion(
        stateEvent: StateEvent,
        authToken: AuthToken
    ): Flow<DataState<MainViewState>>

    fun solveQuestion(
        stateEvent: StateEvent,
        authToken: AuthToken,
        userKey: String
    ): Flow<DataState<MainViewState>>

}