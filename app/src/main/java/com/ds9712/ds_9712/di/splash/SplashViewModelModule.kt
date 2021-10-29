package com.ds9712.ds_9712.di.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ds9712.ds_9712.di.splash.keys.SplashViewModelKey
import com.ds9712.ds_9712.ui.splash.SplashViewModel
import com.ds9712.ds_9712.viewmodels.SplashViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@Module
abstract class SplashViewModelModule {

    @SplashScope
    @Binds
    abstract fun bindViewModelFactory(factory: SplashViewModelFactory): ViewModelProvider.Factory

    @FlowPreview
    @ExperimentalCoroutinesApi
    @SplashScope
    @Binds
    @IntoMap
    @SplashViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel

}