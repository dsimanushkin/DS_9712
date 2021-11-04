package com.ds9712.ds_9712.repository.main

import android.app.Application
import android.content.SharedPreferences
import com.ds9712.ds_9712.api.GenericResponse
import com.ds9712.ds_9712.api.auth.response.CurrentQuestionResponse
import com.ds9712.ds_9712.api.main.DS9712MainService
import com.ds9712.ds_9712.di.main.MainScope
import com.ds9712.ds_9712.models.AuthToken
import com.ds9712.ds_9712.persistence.AccountPropertiesDao
import com.ds9712.ds_9712.persistence.AuthTokenDao
import com.ds9712.ds_9712.repository.safeApiCall
import com.ds9712.ds_9712.session.SessionManager
import com.ds9712.ds_9712.ui.main.state.MainViewState
import com.ds9712.ds_9712.util.ApiResponseHandler
import com.ds9712.ds_9712.util.DataState
import com.ds9712.ds_9712.util.ErrorHandling.Companion.handleErrors
import com.ds9712.ds_9712.util.Response
import com.ds9712.ds_9712.util.StateEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@FlowPreview
@MainScope
class MainRepositoryImpl
@Inject
constructor(
    val application: Application,
    private val DS9712MainService: DS9712MainService,
    val sessionManager: SessionManager,
    val sharedPreferences: SharedPreferences,
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao
): MainRepository {
    override fun getCurrentQuestion(
        stateEvent: StateEvent,
        authToken: AuthToken
    ): Flow<DataState<MainViewState>> = flow {
        val apiResult = safeApiCall(IO) {
            DS9712MainService.getCurrentQuestion(
                authToken = authToken.authToken!!
            )
        }
        emit(
            object : ApiResponseHandler<MainViewState, CurrentQuestionResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: CurrentQuestionResponse): DataState<MainViewState> {
                    if (resultObj.status == handleErrors(9003, application)) {
                        return DataState.error(
                            response = Response(
                                message = handleErrors(resultObj.statusCode, application),
                                statusCode = resultObj.statusCode,
                                requestCode = 2
                            ),
                            stateEvent = stateEvent
                        )
                    }

                    if (resultObj.question == null) {
                        return DataState.data(
                            response = Response(
                                message = handleErrors(resultObj.statusCode, application),
                                statusCode = resultObj.statusCode,
                                requestCode = 2
                            ),
                            data = null,
                            stateEvent = stateEvent
                        )
                    } else {
                        val viewState = MainViewState(
                            currentQuestionFields = MainViewState.CurrentQuestionFields(
                                currentQuestion = resultObj.toCurrentQuestion()
                            )
                        )

                        return DataState.data(
                            response = null,
                            data = viewState,
                            stateEvent = stateEvent
                        )
                    }
                }
            }.getResult()
        )
    }

    override fun solveQuestion(
        stateEvent: StateEvent,
        authToken: AuthToken,
        userKey: String
    ): Flow<DataState<MainViewState>> = flow {
        val apiResult = safeApiCall(IO) {
            DS9712MainService.solveQuestion(
                authToken = authToken.authToken!!,
                userKey = userKey
            )
        }
        emit(
            object : ApiResponseHandler<MainViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: GenericResponse): DataState<MainViewState> {
                    if (resultObj.status == handleErrors(9003, application)) {
                        return DataState.error(
                            response = Response(
                                message = handleErrors(resultObj.statusCode, application),
                                statusCode = resultObj.statusCode,
                                requestCode = 1
                            ),
                            stateEvent = stateEvent
                        )
                    }

                    return DataState.data(
                        response = Response(
                            message = handleErrors(resultObj.statusCode, application),
                            statusCode = resultObj.statusCode,
                            requestCode = 1
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }

            }.getResult()
        )
    }
}