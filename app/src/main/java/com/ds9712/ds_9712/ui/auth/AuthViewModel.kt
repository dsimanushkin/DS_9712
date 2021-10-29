package com.ds9712.ds_9712.ui.auth

import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.models.AuthToken
import com.ds9712.ds_9712.repository.auth.AuthRepository
import com.ds9712.ds_9712.session.SessionManager
import com.ds9712.ds_9712.ui.BaseViewModel
import com.ds9712.ds_9712.ui.auth.state.*
import com.ds9712.ds_9712.util.DataState
import com.ds9712.ds_9712.util.ErrorHandling.Companion.UNKNOWN_ERROR
import com.ds9712.ds_9712.util.Response
import com.ds9712.ds_9712.util.StateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@AuthScope
class AuthViewModel
@Inject
constructor(
    val sessionManager: SessionManager,
    private val authRepository: AuthRepository
): BaseViewModel<AuthViewState>() {
    override fun handleNewData(data: AuthViewState) {
        data.authToken?.let { authToken ->
            setAuthToken(authToken)
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        sessionManager.cachedToken.value?.let { authToken ->
            val job2: Flow<DataState<AuthViewState>> = when(stateEvent) {
//                is AuthStateEvent.AccountStatusEvent -> {
//                    authRepository.accountStatus(
//                        stateEvent = stateEvent,
//                        authToken = authToken
//                    )
//                }
//                is AuthStateEvent.AcceptAgreementEvent -> {
//                    authRepository.acceptAgreement(
//                        stateEvent = stateEvent,
//                        authToken = authToken
//                    )
//                }
//                is AuthStateEvent.DeclineAgreementEvent -> {
//                    authRepository.declineAgreement(
//                        stateEvent = stateEvent,
//                        authToken = authToken
//                    )
//                }
                else -> {
                    flow {
                        emit(
                            DataState.error<AuthViewState>(
                                response = Response(
                                    message = UNKNOWN_ERROR,
                                    statusCode = -1,
                                    requestCode = -1
                                ),
                                stateEvent = stateEvent
                            )
                        )
                    }
                }
            }
            launchJob(stateEvent, job2)
        }

        val job: Flow<DataState<AuthViewState>> = when(stateEvent) {
//            is AuthStateEvent.CheckPreviousAuthEvent -> {
//                authRepository.checkPreviousAuthUser(stateEvent)
//            }
//            is AuthStateEvent.LoginEvent -> {
//                authRepository.login(
//                    stateEvent = stateEvent,
//                    username = stateEvent.username,
//                    password = stateEvent.password
//                )
//            }
//            is AuthStateEvent.ValidateSignUpStepOneFieldsEvent -> {
//                authRepository.validateSignUpStepOneFields(
//                    stateEvent = stateEvent,
//                    fullName = stateEvent.fullName,
//                    username = stateEvent.username
//                )
//            }
//            is AuthStateEvent.ValidateSignUpStepTwoFieldsEvent -> {
//                authRepository.validateSignUpStepTwoFields(
//                    stateEvent = stateEvent,
//                    email = stateEvent.email
//                )
//            }
//            is AuthStateEvent.ValidateSignUpStepThreeEvent -> {
//                authRepository.validateSignUpStepThreeFields(
//                    stateEvent = stateEvent,
//                    password = stateEvent.password,
//                    confirmPassword = stateEvent.confirmPassword
//                )
//            }
//            is AuthStateEvent.ValidateSignUpStepFourEvent -> {
//                authRepository.validateSignUpStepFourFields(
//                    stateEvent = stateEvent,
//                    dateOfBirth = stateEvent.dateOfBirth
//                )
//            }
//            is AuthStateEvent.SignUpEvent -> {
//                authRepository.signUp(
//                    stateEvent = stateEvent,
//                    fullName = stateEvent.fullName,
//                    username = stateEvent.username,
//                    email = stateEvent.email,
//                    password = stateEvent.password,
//                    dateOfBirth = stateEvent.dateOfBirth,
//                    payPalUsername = stateEvent.payPalUsername
//                )
//            }
//            is AuthStateEvent.ResetAccountPasswordEvent -> {
//                authRepository.resetAccountPassword(
//                    stateEvent = stateEvent,
//                    username = stateEvent.username
//                )
//            }
            else -> {
                flow {
                    emit(
                        DataState.error<AuthViewState>(
                            response = Response(
                                message = UNKNOWN_ERROR,
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

    private fun setAuthToken(authToken: AuthToken) {
        val update = getCurrentViewStateOrNew()
        if (update.authToken == authToken) {
            return
        }
        update.authToken = authToken
        setViewState(update)
    }

    fun resetAuthToken() {
        val update = getCurrentViewStateOrNew()
        update.authToken = null
        setViewState(update)
    }

    fun resetAccountStatus() {
        val update = getCurrentViewStateOrNew()
        update.accountStatus = null
        setViewState(update)
    }

    fun setLoginFields(loginFields: LoginFields) {
        val update = getCurrentViewStateOrNew()
        if (update.loginFields == loginFields) {
            return
        }
        update.loginFields = loginFields
        setViewState(update)
    }

    fun setSignUpStepOneFields(signUpStepOneFields: SignUpStepOneFields) {
        val update = getCurrentViewStateOrNew()
        if (update.signUpStepOneFields == signUpStepOneFields) {
            return
        }
        update.signUpStepOneFields = signUpStepOneFields
        setViewState(update)
    }

    fun setForgotPasswordFields(forgotPasswordFields: ForgotPasswordFields) {
        val update = getCurrentViewStateOrNew()
        if (update.forgotPasswordFields == forgotPasswordFields) {
            return
        }
        update.forgotPasswordFields = forgotPasswordFields
        setViewState(update)
    }

    fun setSignUpStepTwoFields(signUpStepTwoFields: SignUpStepTwoFields) {
        val update = getCurrentViewStateOrNew()
        if (update.signUpStepTwoFields == signUpStepTwoFields) {
            return
        }
        update.signUpStepTwoFields = signUpStepTwoFields
        setViewState(update)
    }

    fun setSignUpStepThreeFields(signUpStepThreeFields: SignUpStepThreeFields) {
        val update = getCurrentViewStateOrNew()
        if (update.signUpStepThreeFields == signUpStepThreeFields) {
            return
        }
        update.signUpStepThreeFields = signUpStepThreeFields
        setViewState(update)
    }

    fun setSignUpStepFourFields(signUpStepFourFields: SignUpStepFourFields) {
        val update = getCurrentViewStateOrNew()
        if (update.signUpStepFourFields == signUpStepFourFields) {
            return
        }
        update.signUpStepFourFields = signUpStepFourFields
        setViewState(update)
    }

    fun setSignUpStepFiveFields(signUpFields: SignUpFields) {
        val update = getCurrentViewStateOrNew()
        if (update.signUpFields == signUpFields) {
            return
        }
        update.signUpFields = signUpFields
        setViewState(update)
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    fun logout() {
        sessionManager.logout()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}