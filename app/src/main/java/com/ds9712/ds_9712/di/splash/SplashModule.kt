package com.ds9712.ds_9712.di.splash

import android.app.Application
import android.content.SharedPreferences
import com.ds9712.ds_9712.persistence.AccountPropertiesDao
import com.ds9712.ds_9712.persistence.AuthTokenDao
import com.ds9712.ds_9712.repository.splash.SplashRepository
import com.ds9712.ds_9712.repository.splash.SplashRepositoryImpl
import com.ds9712.ds_9712.session.SessionManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview

@FlowPreview
@Module
object SplashModule {
    @JvmStatic
    @SplashScope
    @Provides
    fun provideSplashRepository(
        application: Application,
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        sharedPreferences: SharedPreferences
    ): SplashRepository {
        return SplashRepositoryImpl(
            application,
            sessionManager,
            authTokenDao,
            accountPropertiesDao,
            sharedPreferences
        )
    }
}