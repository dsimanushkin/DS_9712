package com.ds9712.ds_9712

import android.app.Application
import com.ds9712.ds_9712.di.AppComponent
import com.ds9712.ds_9712.di.DaggerAppComponent
import com.ds9712.ds_9712.di.auth.AuthComponent
import com.ds9712.ds_9712.di.intro.IntroComponent
import com.ds9712.ds_9712.di.main.MainComponent
import com.ds9712.ds_9712.di.splash.SplashComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class BaseApplication: Application() {

    lateinit var appComponent: AppComponent
    private var splashComponent: SplashComponent? = null
    private var introComponent: IntroComponent? = null
    private var authComponent: AuthComponent? = null
    private var mainComponent: MainComponent? = null

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    fun splashComponent(): SplashComponent {
        if (splashComponent == null) {
            splashComponent = appComponent.splashComponent().create()
        }
        return splashComponent as SplashComponent
    }

    fun introComponent(): IntroComponent {
        if (introComponent == null) {
            introComponent = appComponent.introComponent().create()
        }
        return introComponent as IntroComponent
    }

    fun authComponent(): AuthComponent {
        if (authComponent == null) {
            authComponent = appComponent.authComponent().create()
        }
        return authComponent as AuthComponent
    }

    fun mainComponent(): MainComponent {
        if (mainComponent == null) {
            mainComponent = appComponent.mainComponent().create()
        }
        return mainComponent as MainComponent
    }

    fun releaseSplashComponent() {
        splashComponent = null
    }

    fun releaseIntroComponent() {
        introComponent = null
    }

    fun releaseAuthComponent() {
        authComponent = null
    }

    fun releaseMainComponent() {
        mainComponent = null
    }

}