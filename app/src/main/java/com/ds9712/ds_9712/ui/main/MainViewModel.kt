package com.ds9712.ds_9712.ui.main

import com.ds9712.ds_9712.di.main.MainScope
import com.ds9712.ds_9712.models.CurrentQuestion
import com.ds9712.ds_9712.repository.main.MainRepository
import com.ds9712.ds_9712.session.SessionManager
import com.ds9712.ds_9712.ui.BaseViewModel
import com.ds9712.ds_9712.ui.main.state.MainStateEvent
import com.ds9712.ds_9712.ui.main.state.MainViewState
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
@MainScope
//@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val mainRepository: MainRepository,
): BaseViewModel<MainViewState>() {
    override fun handleNewData(data: MainViewState) {
        data.currentQuestionFields.currentQuestion.let { currentQuestion ->
            setCurrentQuestion(currentQuestion!!)
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        sessionManager.cachedToken.value?.let { authToken ->
            val job1: Flow<DataState<MainViewState>> = when(stateEvent) {
                is MainStateEvent.GetCurrentQuestionEvent -> {
                    mainRepository.getCurrentQuestion(
                        stateEvent = stateEvent,
                        authToken = authToken
                    )
                }
                is MainStateEvent.SolveQuestionEvent -> {
                    mainRepository.solveQuestion(
                        stateEvent = stateEvent,
                        authToken = authToken,
                        userKey = stateEvent.userKey
                    )
                }
                else -> {
                    flow {
                        emit(
                            DataState.error<MainViewState>(
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
            launchJob(stateEvent, job1)
        }
    }

    private fun setCurrentQuestion(currentQuestion: CurrentQuestion) {
        val update = getCurrentViewStateOrNew()
        if (update.currentQuestionFields.currentQuestion == currentQuestion) {
            return
        }
        update.currentQuestionFields.currentQuestion = currentQuestion
        setViewState(update)
    }

    override fun initNewViewState(): MainViewState {
        return MainViewState()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}