package com.ds9712.ds_9712.ui.splash

import com.ds9712.ds_9712.di.splash.SplashScope
import com.ds9712.ds_9712.models.AuthToken
import com.ds9712.ds_9712.repository.splash.SplashRepository
import com.ds9712.ds_9712.session.SessionManager
import com.ds9712.ds_9712.ui.BaseViewModel
import com.ds9712.ds_9712.ui.splash.state.SplashStateEvent
import com.ds9712.ds_9712.ui.splash.state.SplashViewState
import com.ds9712.ds_9712.util.DataState
import com.ds9712.ds_9712.util.ErrorHandling
import com.ds9712.ds_9712.util.Response
import com.ds9712.ds_9712.util.StateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@SplashScope
class SplashViewModel
@Inject
constructor(
    val sessionManager: SessionManager,
    private val splashRepository: SplashRepository
): BaseViewModel<SplashViewState>() {
    override fun handleNewData(data: SplashViewState) {
        data.authToken?.let { authToken ->
            setAuthToken(authToken)
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<SplashViewState>> = when(stateEvent) {
            is SplashStateEvent.CheckPreviousAuthEvent -> {
                splashRepository.checkPreviousAuthUser(stateEvent)
            }
            else -> {
                flow {
                    emit(
                        DataState.error<SplashViewState>(
                            response = Response(
                                message = ErrorHandling.UNKNOWN_ERROR,
                                statusCode = -1,
                                requestCode = -1
                            ),
                            stateEvent = stateEvent
                        )
                    )
                }
            }
        }
        launchJob(stateEvent, job)
    }

    override fun initNewViewState(): SplashViewState {
        return SplashViewState()
    }

    private fun setAuthToken(authToken: AuthToken) {
        val update = getCurrentViewStateOrNew()
        if (update.authToken == authToken) {
            return
        }
        update.authToken = authToken
        setViewState(update)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}