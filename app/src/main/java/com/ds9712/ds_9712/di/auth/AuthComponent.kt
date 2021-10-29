package com.ds9712.ds_9712.di.auth

import com.ds9712.ds_9712.ui.auth.AuthActivity
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@AuthScope
@Subcomponent(
    modules = [
        AuthModule::class,
        AuthViewModelModule::class,
        AuthFragmentsModule::class
    ]
)
interface AuthComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthComponent
    }

    @ExperimentalCoroutinesApi
    fun inject(authActivity: AuthActivity)
}