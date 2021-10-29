package com.ds9712.ds_9712.repository.main

import android.app.Application
import android.content.SharedPreferences
import com.ds9712.ds_9712.api.main.DS9712MainService
import com.ds9712.ds_9712.di.main.MainScope
import com.ds9712.ds_9712.persistence.AccountPropertiesDao
import com.ds9712.ds_9712.persistence.AuthTokenDao
import com.ds9712.ds_9712.session.SessionManager
import kotlinx.coroutines.FlowPreview
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
}