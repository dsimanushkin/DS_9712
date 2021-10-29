package com.ds9712.ds_9712.di.main

import android.app.Application
import android.content.SharedPreferences
import com.ds9712.ds_9712.api.main.DS9712MainService
import com.ds9712.ds_9712.persistence.AccountPropertiesDao
import com.ds9712.ds_9712.persistence.AuthTokenDao
import com.ds9712.ds_9712.repository.main.MainRepository
import com.ds9712.ds_9712.repository.main.MainRepositoryImpl
import com.ds9712.ds_9712.session.SessionManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import retrofit2.Retrofit

@FlowPreview
@Module
object MainModule {

    @JvmStatic
    @MainScope
    @Provides
    fun provideDS9712MainService(retrofitBuilder: Retrofit.Builder): DS9712MainService {
        return retrofitBuilder
            .build()
            .create(DS9712MainService::class.java)
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideMainRepository(
        application: Application,
        DS9712MainService: DS9712MainService,
        sessionManager: SessionManager,
        sharedPreferences: SharedPreferences,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao
    ): MainRepository {
        return MainRepositoryImpl(
            application,
            DS9712MainService,
            sessionManager,
            sharedPreferences,
            authTokenDao,
            accountPropertiesDao
        )
    }
}