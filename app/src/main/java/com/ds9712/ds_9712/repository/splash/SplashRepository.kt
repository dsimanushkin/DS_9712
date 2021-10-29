package com.ds9712.ds_9712.repository.splash

import com.ds9712.ds_9712.di.splash.SplashScope
import com.ds9712.ds_9712.ui.splash.state.SplashViewState
import com.ds9712.ds_9712.util.DataState
import com.ds9712.ds_9712.util.StateEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@SplashScope
interface SplashRepository {

    fun checkPreviousAuthUser(
        stateEvent: StateEvent
    ): Flow<DataState<SplashViewState>>

}