package com.ds9712.ds_9712.repository.splash

import android.app.Application
import android.content.SharedPreferences
import com.ds9712.ds_9712.di.splash.SplashScope
import com.ds9712.ds_9712.models.AccountProperties
import com.ds9712.ds_9712.persistence.AccountPropertiesDao
import com.ds9712.ds_9712.persistence.AuthTokenDao
import com.ds9712.ds_9712.repository.safeCacheCall
import com.ds9712.ds_9712.session.SessionManager
import com.ds9712.ds_9712.ui.splash.state.SplashViewState
import com.ds9712.ds_9712.util.CacheResponseHandler
import com.ds9712.ds_9712.util.DataState
import com.ds9712.ds_9712.util.PreferenceKeys
import com.ds9712.ds_9712.util.StateEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@FlowPreview
@SplashScope
class SplashRepositoryImpl
@Inject
constructor(
    val application: Application,
    val sessionManager: SessionManager,
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val sharedPreferences: SharedPreferences
): SplashRepository {
    override fun checkPreviousAuthUser(stateEvent: StateEvent): Flow<DataState<SplashViewState>> = flow {
        val previousAuthUserUsername: String? = sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)
        if(!previousAuthUserUsername.isNullOrBlank()){
            val apiResult = safeCacheCall(Dispatchers.IO){
                accountPropertiesDao.searchByUsername(previousAuthUserUsername)
            }
            emit(
                object: CacheResponseHandler<SplashViewState, AccountProperties>(
                    response = apiResult,
                    stateEvent = stateEvent
                ){
                    override suspend fun handleSuccess(resultObj: AccountProperties): DataState<SplashViewState> {
                        if(resultObj.id.isNotEmpty()){
                            authTokenDao.searchById(resultObj.id).let { authToken ->
                                if(authToken != null){
                                    if(authToken.authToken != null){
                                        return DataState.data(
                                            data = SplashViewState(
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
}