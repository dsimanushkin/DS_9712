package com.ds9712.ds_9712.di.auth

import android.app.Application
import android.content.SharedPreferences
import com.ds9712.ds_9712.api.auth.DS9712AuthService
import com.ds9712.ds_9712.persistence.AccountPropertiesDao
import com.ds9712.ds_9712.persistence.AuthTokenDao
import com.ds9712.ds_9712.repository.auth.AuthRepository
import com.ds9712.ds_9712.repository.auth.AuthRepositoryImpl
import com.ds9712.ds_9712.session.SessionManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import retrofit2.Retrofit

@FlowPreview
@Module
object AuthModule {

    @JvmStatic
    @AuthScope
    @Provides
    fun provideDS9712AuthService(retrofitBuilder: Retrofit.Builder): DS9712AuthService {
        return retrofitBuilder
            .build()
            .create(DS9712AuthService::class.java)
    }

    @JvmStatic
    @AuthScope
    @Provides
    fun provideAuthRepository(
        application: Application,
        sessionManager: SessionManager,
        DS9712AuthService: DS9712AuthService,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        sharedPreferences: SharedPreferences,
        editor: SharedPreferences.Editor
    ): AuthRepository {
        return AuthRepositoryImpl(
            application,
            sessionManager,
            DS9712AuthService,
            authTokenDao,
            accountPropertiesDao,
            sharedPreferences,
            editor
        )
    }
}