package com.ds9712.ds_9712.repository.auth

import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.models.AuthToken
import com.ds9712.ds_9712.ui.auth.state.AuthViewState
import com.ds9712.ds_9712.util.DataState
import com.ds9712.ds_9712.util.StateEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import java.util.*

@FlowPreview
@AuthScope
interface AuthRepository {

    fun accountStatus(
        stateEvent: StateEvent,
        authToken: AuthToken
    ): Flow<DataState<AuthViewState>>

    fun checkPreviousAuthUser(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>>

    fun login(
        stateEvent: StateEvent,
        username: String,
        password: String
    ): Flow<DataState<AuthViewState>>

    fun saveAuthenticatedUserToPrefs(username: String)

    fun validateSignUpStepOneFields(
        stateEvent: StateEvent,
        fullName: String,
        username: String
    ): Flow<DataState<AuthViewState>>

    fun validateSignUpStepTwoFields(
        stateEvent: StateEvent,
        email: String
    ): Flow<DataState<AuthViewState>>

    fun validateSignUpStepThreeFields(
        stateEvent: StateEvent,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>>

    fun signUp(
        stateEvent: StateEvent,
        fullName: String,
        username: String,
        email: String,
        password: String,
        dateOfBirth: Calendar
    ): Flow<DataState<AuthViewState>>

    fun acceptAgreement(
        stateEvent: StateEvent,
        authToken: AuthToken
    ): Flow<DataState<AuthViewState>>

    fun declineAgreement(
        stateEvent: StateEvent,
        authToken: AuthToken
    ): Flow<DataState<AuthViewState>>

    fun verifyEmail(
        stateEvent: StateEvent,
        authToken: AuthToken,
        verificationCode: String
    ): Flow<DataState<AuthViewState>>

    fun resendVerificationCode(
        stateEvent: StateEvent,
        authToken: AuthToken
    ): Flow<DataState<AuthViewState>>

    fun changeEmailAddress(
        stateEvent: StateEvent,
        authToken: AuthToken,
        email: String,
        password: String
    ): Flow<DataState<AuthViewState>>
}