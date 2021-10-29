package com.ds9712.ds_9712.di

import com.ds9712.ds_9712.di.auth.AuthComponent
import com.ds9712.ds_9712.di.intro.IntroComponent
import com.ds9712.ds_9712.di.main.MainComponent
import com.ds9712.ds_9712.di.splash.SplashComponent
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@Module(
    subcomponents = [
        SplashComponent::class,
        IntroComponent::class,
        AuthComponent::class,
        MainComponent::class
    ]
)
class SubComponentsModule