package com.ds9712.ds_9712.di.splash

import com.ds9712.ds_9712.ui.splash.SplashActivity
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@SplashScope
@Subcomponent(
    modules = [
        SplashModule::class,
        SplashViewModelModule::class
    ]
)
interface SplashComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): SplashComponent
    }

    @ExperimentalCoroutinesApi
    fun inject(splashActivity: SplashActivity)
}