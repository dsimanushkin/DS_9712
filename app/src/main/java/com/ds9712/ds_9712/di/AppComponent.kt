package com.ds9712.ds_9712.di

import android.app.Application
import com.ds9712.ds_9712.di.auth.AuthComponent
import com.ds9712.ds_9712.di.intro.IntroComponent
import com.ds9712.ds_9712.di.main.MainComponent
import com.ds9712.ds_9712.di.splash.SplashComponent
import com.ds9712.ds_9712.session.SessionManager
import com.ds9712.ds_9712.ui.BaseActivity
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Singleton
@Component(
    modules = [
        AppModule::class,
        SubComponentsModule::class
    ]
)
interface AppComponent {
    val sessionManager: SessionManager

    fun inject(baseActivity: BaseActivity)

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    @FlowPreview
    fun splashComponent(): SplashComponent.Factory

    @FlowPreview
    fun introComponent(): IntroComponent.Factory

    @FlowPreview
    fun authComponent(): AuthComponent.Factory

    @FlowPreview
    fun mainComponent(): MainComponent.Factory
}