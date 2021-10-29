package com.ds9712.ds_9712.repository.auth

import android.app.Application
import android.content.SharedPreferences
import com.ds9712.ds_9712.api.auth.DS9712AuthService
import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.persistence.AccountPropertiesDao
import com.ds9712.ds_9712.persistence.AuthTokenDao
import com.ds9712.ds_9712.session.SessionManager
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

}