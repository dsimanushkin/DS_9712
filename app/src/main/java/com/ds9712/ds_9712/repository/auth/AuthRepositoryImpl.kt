package com.ds9712.ds_9712.repository.auth

import android.app.Application
import android.content.SharedPreferences
import com.ds9712.ds_9712.api.GenericResponse
import com.ds9712.ds_9712.api.auth.DS9712AuthService
import com.ds9712.ds_9712.api.auth.response.AccountStatusResponse
import com.ds9712.ds_9712.api.auth.response.LoginResponse
import com.ds9712.ds_9712.api.auth.response.SignUpResponse
import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.models.AccountProperties
import com.ds9712.ds_9712.models.AccountStatus
import com.ds9712.ds_9712.models.AuthToken
import com.ds9712.ds_9712.persistence.AccountPropertiesDao
import com.ds9712.ds_9712.persistence.AuthTokenDao
import com.ds9712.ds_9712.repository.buildError
import com.ds9712.ds_9712.repository.safeApiCall
import com.ds9712.ds_9712.repository.safeCacheCall
import com.ds9712.ds_9712.session.SessionManager
import com.ds9712.ds_9712.ui.auth.state.*
import com.ds9712.ds_9712.util.*
import com.ds9712.ds_9712.util.ErrorHandling.Companion.handleErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

@FlowPreview
@AuthScope
class AuthRepositoryImpl
@Inject
constructor(
    val application: Application,
    val sessionManager: SessionManager,
    val DS9712AuthService: DS9712AuthService,
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val sharedPreferences: SharedPreferences,
    val editor: SharedPreferences.Editor
): AuthRepository {
    override fun accountStatus(
        stateEvent: StateEvent,
        authToken: AuthToken
    ): Flow<DataState<AuthViewState>> = flow {
        val apiResult = safeApiCall(Dispatchers.IO) {
            DS9712AuthService.accountStatus(
                authToken = authToken.authToken!!
            )
        }
        emit(
            object: ApiResponseHandler<AuthViewState, AccountStatusResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: AccountStatusResponse): DataState<AuthViewState> {
                    // Login Errors counts as 200 response from server, need to handle that
                    if(resultObj.status == handleErrors(9003, application)){
                        return DataState.error(
                            response = Response(
                                message = handleErrors(resultObj.statusCode, application),
                                statusCode = resultObj.statusCode,
                                requestCode = 0
                            ),
                            stateEvent = stateEvent
                        )
                    }

                    return DataState.data(
                        response = null,
                        data = AuthViewState(
                            accountStatus = AccountStatus(
                                resultObj.isAgreementAccepted,
                                resultObj.isEmailConfirmed
                            )
                        ),
                        stateEvent = stateEvent,
                    )
                }
            }.getResult()
        )
    }

    override fun checkPreviousAuthUser(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>> = flow {
        val previousAuthUserUsername: String? = sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)
        if(!previousAuthUserUsername.isNullOrBlank()){
            val apiResult = safeCacheCall(Dispatchers.IO){
                accountPropertiesDao.searchByUsername(previousAuthUserUsername)
            }
            emit(
                object: CacheResponseHandler<AuthViewState, AccountProperties>(
                    response = apiResult,
                    stateEvent = stateEvent
                ){
                    override suspend fun handleSuccess(resultObj: AccountProperties): DataState<AuthViewState> {
                        if(resultObj.id.isNotEmpty()){
                            authTokenDao.searchById(resultObj.id).let { authToken ->
                                if(authToken != null){
                                    if(authToken.authToken != null){
                                        return DataState.data(
                                            data = AuthViewState(
                                                authToken = authToken
                                            ),
                                            response = null,
                                            stateEvent = stateEvent
                                        )
                                    }
                                }
                            }
                        }
                        return DataState.data(
                            response = null,
                            stateEvent = stateEvent
                        )
                    }
                }.getResult()
            )
        }
    }

    override fun login(
        stateEvent: StateEvent,
        username: String,
        password: String
    ): Flow<DataState<AuthViewState>> = flow {
        val loginFieldErrors = LoginFields(username, password).isValidForSubmission(application)
        if (loginFieldErrors == LoginFields.LoginError.none(application)) {
            val apiResult = safeApiCall(Dispatchers.IO) {
                DS9712AuthService.login(
                    username = username,
                    password = password
                )
            }
            emit(
                object: ApiResponseHandler<AuthViewState, LoginResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: LoginResponse): DataState<AuthViewState> {
                        // Login Errors counts as 200 response from server, need to handle that
                        if(resultObj.status == handleErrors(9003, application)){
                            return DataState.error(
                                response = Response(
                                    handleErrors(resultObj.statusCode, application),
                                    statusCode = resultObj.statusCode,
                                    requestCode = 1
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        val result1 = accountPropertiesDao.insertAndReplace(
                            AccountProperties(
                                id = resultObj.id!!,
                                shortId = resultObj.shortId!!,
                                username = resultObj.username!!
                            )
                        )
                        // will return -1 if failure
                        if(result1 < 0){
                            return DataState.error(
                                response = Response(
                                    handleErrors(9005, application),
                                    resultObj.statusCode,
                                    requestCode = 1
                                ),
                                stateEvent = stateEvent
                            )
                        }

                        // will return -1 if failure
                        val authToken = AuthToken(
                            accountId = resultObj.id!!,
                            shortId = resultObj.shortId,
                            authToken = resultObj.authToken
                        )
                        val result2 = authTokenDao.insert(authToken)
                        if(result2 < 0){
                            return DataState.error(
                                response = Response(
                                    handleErrors(9004, application),
                                    resultObj.statusCode,
                                    requestCode = 1
                                ),
                                stateEvent = stateEvent
                            )
                        }

                        saveAuthenticatedUserToPrefs(username)

                        return DataState.data(
                            data = AuthViewState(
                                authToken = authToken
                            ),
                            stateEvent = stateEvent,
                            response = Response(
                                message = handleErrors(resultObj.statusCode, application),
                                resultObj.statusCode,
                                requestCode = 1
                            )
                        )
                    }

                }.getResult()
            )
        } else {
            emit(
                buildError<AuthViewState>(
                    loginFieldErrors,
                    -2,
                    1,
                    stateEvent
                )
            )
        }
    }

    override fun saveAuthenticatedUserToPrefs(username: String) {
        editor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, username)
        editor.apply()
    }

    override fun validateSignUpStepOneFields(
        stateEvent: StateEvent,
        fullName: String,
        username: String
    ): Flow<DataState<AuthViewState>> = flow {

        val signUpStepOneFieldErrors = SignUpStepOneFields(fullName, username).isValidForSubmission(application)
        if (signUpStepOneFieldErrors == SignUpStepOneFields.SignUpStepOneError.none(application)) {
            val apiResult = safeApiCall(Dispatchers.IO) {
                DS9712AuthService.signUpStepOneFieldsValidation(
                    fullName = fullName,
                    username = username
                )
            }
            emit(
                object: ApiResponseHandler<AuthViewState, GenericResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: GenericResponse): DataState<AuthViewState> {
                        // Login Errors counts as 200 response from server, need to handle that
                        if(resultObj.status == handleErrors(9003, application)){
                            return DataState.error(
                                response = Response(
                                    message = handleErrors(resultObj.statusCode, application),
                                    statusCode = resultObj.statusCode,
                                    requestCode = 3
                                ),
                                stateEvent = stateEvent
                            )
                        }

                        return DataState.data(
                            response = Response(
                                message = handleErrors(resultObj.statusCode, application),
                                statusCode = resultObj.statusCode,
                                requestCode = 3
                            ),
                            data = null,
                            stateEvent = stateEvent,
                        )
                    }
                }.getResult()
            )
        } else {
            emit(
                buildError<AuthViewState>(
                    signUpStepOneFieldErrors,
                    -2,
                    3,
                    stateEvent
                )
            )
        }
    }

    override fun validateSignUpStepTwoFields(
        stateEvent: StateEvent,
        email: String
    ): Flow<DataState<AuthViewState>> = flow {
        val signUpStepTwoFieldErrors = SignUpStepTwoFields(email).isValidForSubmission(application)
        if (signUpStepTwoFieldErrors == SignUpStepTwoFields.SignUpStepTwoError.none(application)) {
            val apiResult = safeApiCall(Dispatchers.IO) {
                DS9712AuthService.signUpStepTwoFieldsValidation(
                    email = email
                )
            }
            emit(
                object: ApiResponseHandler<AuthViewState, GenericResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: GenericResponse): DataState<AuthViewState> {
                        // Login Errors counts as 200 response from server, need to handle that
                        if(resultObj.status == handleErrors(9003, application)){
                            return DataState.error(
                                response = Response(
                                    message = handleErrors(resultObj.statusCode, application),
                                    statusCode = resultObj.statusCode,
                                    requestCode = 4
                                ),
                                stateEvent = stateEvent
                            )
                        }

                        return DataState.data(
                            response = Response(
                                message = handleErrors(resultObj.statusCode, application),
                                statusCode = resultObj.statusCode,
                                requestCode = 4
                            ),
                            data = null,
                            stateEvent = stateEvent,
                        )
                    }
                }.getResult()
            )
        } else {
            emit(
                buildError<AuthViewState>(
                    signUpStepTwoFieldErrors,
                    -2,
                    4,
                    stateEvent
                )
            )
        }
    }

    override fun validateSignUpStepThreeFields(
        stateEvent: StateEvent,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>> = flow {
        val signUpStepThreeFieldErrors = SignUpStepThreeFields(password, confirmPassword).isValidForSubmission(application)
        if (signUpStepThreeFieldErrors == SignUpStepThreeFields.SignUpStepThreeError.none(application)) {
            val apiResult = safeApiCall(Dispatchers.IO) {
                DS9712AuthService.signUpStepThreeFieldsValidation(
                    password = password
                )
            }
            emit(
                object: ApiResponseHandler<AuthViewState, GenericResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: GenericResponse): DataState<AuthViewState> {
                        // Login Errors counts as 200 response from server, need to handle that
                        if(resultObj.status == handleErrors(9003, application)){
                            return DataState.error(
                                response = Response(
                                    message = handleErrors(resultObj.statusCode, application),
                                    statusCode = resultObj.statusCode,
                                    requestCode = 5
                                ),
                                stateEvent = stateEvent
                            )
                        }

                        return DataState.data(
                            response = Response(
                                message = handleErrors(resultObj.statusCode, application),
                                statusCode = resultObj.statusCode,
                                requestCode = 5
                            ),
                            data = null,
                            stateEvent = stateEvent,
                        )
                    }
                }.getResult()
            )
        } else {
            emit(
                buildError<AuthViewState>(
                    signUpStepThreeFieldErrors,
                    -2,
                    5,
                    stateEvent
                )
            )
        }
    }

    override fun signUp(
        stateEvent: StateEvent,
        fullName: String,
        username: String,
        email: String,
        password: String,
        dateOfBirth: Calendar
    ): Flow<DataState<AuthViewState>> = flow {
        val signUpFieldErrors = SignUpFields(fullName, username, email, password, dateOfBirth).isValidForSubmission(application)
        if (signUpFieldErrors == SignUpFields.SignUpError.none(application)) {
            val apiResult = safeApiCall(Dispatchers.IO) {
                DS9712AuthService.signUp(
                    fullName = fullName,
                    username = username,
                    email = email,
                    password = password,
                    dateOfBirth = "${dateOfBirth.get(Calendar.MONTH) + 1}-${dateOfBirth.get(Calendar.DAY_OF_MONTH)}-${dateOfBirth.get(Calendar.YEAR)}"
                )
            }
            emit(
                object: ApiResponseHandler<AuthViewState, SignUpResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: SignUpResponse): DataState<AuthViewState> {
                        if(resultObj.status == handleErrors(9003, application)){
                            return DataState.error(
                                response = Response(
                                    message = handleErrors(resultObj.statusCode, application),
                                    statusCode = resultObj.statusCode,
                                    requestCode = 6
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        val result1 = accountPropertiesDao.insertAndReplace(
                            AccountProperties(
                                id = resultObj.id,
                                shortId = resultObj.shortId,
                                username = resultObj.username
                            )
                        )
                        // will return -1 if failure
                        if(result1 < 0){
                            return DataState.error(
                                response = Response(
                                    handleErrors(9005, application),
                                    resultObj.statusCode,
                                    requestCode = 6
                                ),
                                stateEvent = stateEvent
                            )
                        }

                        // will return -1 if failure
                        val authToken = AuthToken(
                            accountId = resultObj.id,
                            shortId = resultObj.shortId,
                            authToken = resultObj.authToken
                        )
                        val result2 = authTokenDao.insert(authToken)
                        if(result2 < 0){
                            return DataState.error(
                                response = Response(
                                    handleErrors(9004, application),
                                    resultObj.statusCode,
                                    requestCode = 6
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        saveAuthenticatedUserToPrefs(username)
                        return DataState.data(
                            data = AuthViewState(
                                authToken = authToken
                            ),
                            stateEvent = stateEvent,
                            response = Response(
                                message = handleErrors(resultObj.statusCode, application),
                                statusCode = resultObj.statusCode,
                                requestCode = 6
                            )
                        )
                    }
                }.getResult()
            )
        } else {
            emit(
                buildError<AuthViewState>(
                    signUpFieldErrors,
                    -2,
                    6,
                    stateEvent
                )
            )
        }
    }

    override fun acceptAgreement(
        stateEvent: StateEvent,
        authToken: AuthToken
    ): Flow<DataState<AuthViewState>> = flow {
        val apiResult = safeApiCall(Dispatchers.IO) {
            DS9712AuthService.acceptAgreement(
                authToken = authToken.authToken!!
            )
        }
        emit(
            object: ApiResponseHandler<AuthViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: GenericResponse): DataState<AuthViewState> {
                    // Login Errors counts as 200 response from server, need to handle that
                    if(resultObj.status == handleErrors(9003, application)){
                        return DataState.error(
                            response = Response(
                                message = handleErrors(resultObj.statusCode, application),
                                statusCode = resultObj.statusCode,
                                requestCode = 7
                            ),
                            stateEvent = stateEvent
                        )
                    }

                    return DataState.data(
                        response = Response(
                            message = handleErrors(resultObj.statusCode, application),
                            statusCode = resultObj.statusCode,
                            requestCode = 7
                        ),
                        data = null,
                        stateEvent = stateEvent,
                    )
                }
            }.getResult()
        )
    }

    override fun declineAgreement(
        stateEvent: StateEvent,
        authToken: AuthToken
    ): Flow<DataState<AuthViewState>> = flow {
        val apiResult = safeApiCall(Dispatchers.IO) {
            DS9712AuthService.declineAgreement(
                authToken = authToken.authToken!!
            )
        }
        emit(
            object: ApiResponseHandler<AuthViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: GenericResponse): DataState<AuthViewState> {
                    // Login Errors counts as 200 response from server, need to handle that
                    if(resultObj.status == handleErrors(9003, application)){
                        return DataState.error(
                            response = Response(
                                message = handleErrors(resultObj.statusCode, application),
                                statusCode = resultObj.statusCode,
                                requestCode = 7
                            ),
                            stateEvent = stateEvent
                        )
                    }

                    return DataState.data(
                        response = Response(
                            message = handleErrors(resultObj.statusCode, application),
                            statusCode = resultObj.statusCode,
                            requestCode = 7
                        ),
                        data = null,
                        stateEvent = stateEvent,
                    )
                }
            }.getResult()
        )
    }

    override fun verifyEmail(
        stateEvent: StateEvent,
        authToken: AuthToken,
        verificationCode: String
    ): Flow<DataState<AuthViewState>> = flow {
        val apiResult = safeApiCall(Dispatchers.IO) {
            DS9712AuthService.verifyEmail(
                authToken = authToken.authToken!!,
                verificationCode = verificationCode
            )
        }
        emit(
            object: ApiResponseHandler<AuthViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: GenericResponse): DataState<AuthViewState> {
                    // Login Errors counts as 200 response from server, need to handle that
                    if(resultObj.status == handleErrors(9003, application)){
                        return DataState.error(
                            response = Response(
                                message = handleErrors(resultObj.statusCode, application),
                                statusCode = resultObj.statusCode,
                                requestCode = 8
                            ),
                            stateEvent = stateEvent
                        )
                    }

                    return DataState.data(
                        response = Response(
                            message = handleErrors(resultObj.statusCode, application),
                            statusCode = resultObj.statusCode,
                            requestCode = 8
                        ),
                        data = null,
                        stateEvent = stateEvent,
                    )
                }
            }.getResult()
        )
    }

    override fun resendVerificationCode(
        stateEvent: StateEvent,
        authToken: AuthToken
    ): Flow<DataState<AuthViewState>> = flow {
        val apiResult = safeApiCall(Dispatchers.IO) {
            DS9712AuthService.resendVerificationCode(
                authToken = authToken.authToken!!
            )
        }
        emit(
            object: ApiResponseHandler<AuthViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: GenericResponse): DataState<AuthViewState> {
                    // Login Errors counts as 200 response from server, need to handle that
                    if(resultObj.status == handleErrors(9003, application)){
                        return DataState.error(
                            response = Response(
                                message = handleErrors(resultObj.statusCode, application),
                                statusCode = resultObj.statusCode,
                                requestCode = 8
                            ),
                            stateEvent = stateEvent
                        )
                    }

                    return DataState.data(
                        response = Response(
                            message = handleErrors(resultObj.statusCode, application),
                            statusCode = resultObj.statusCode,
                            requestCode = 8
                        ),
                        data = null,
                        stateEvent = stateEvent,
                    )
                }
            }.getResult()
        )
    }

    override fun changeEmailAddress(
        stateEvent: StateEvent,
        authToken: AuthToken,
        email: String,
        password: String
    ): Flow<DataState<AuthViewState>> = flow {
        val changeEmailAddressFieldErrors = ChangeEmailAddressFields(email, password).isValidForSubmission(application)
        if (changeEmailAddressFieldErrors == ChangeEmailAddressFields.ChangeEmailAddressError.none(application)) {
            val apiResult = safeApiCall(Dispatchers.IO) {
                DS9712AuthService.changeEmailAddress(
                    authToken = authToken.authToken!!,
                    email = email,
                    password = password
                )
            }
            emit(
                object: ApiResponseHandler<AuthViewState, GenericResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: GenericResponse): DataState<AuthViewState> {
                        // Login Errors counts as 200 response from server, need to handle that
                        if(resultObj.status == handleErrors(9003, application)){
                            return DataState.error(
                                response = Response(
                                    message = handleErrors(resultObj.statusCode, application),
                                    statusCode = resultObj.statusCode,
                                    requestCode = 9
                                ),
                                stateEvent = stateEvent
                            )
                        }

                        return DataState.data(
                            response = Response(
                                message = handleErrors(resultObj.statusCode, application),
                                statusCode = resultObj.statusCode,
                                requestCode = 9
                            ),
                            data = null,
                            stateEvent = stateEvent,
                        )
                    }
                }.getResult()
            )
        } else {
            emit(
                buildError<AuthViewState>(
                    changeEmailAddressFieldErrors,
                    -2,
                    9,
                    stateEvent
                )
            )
        }
    }
}