package com.ds9712.ds_9712.di.intro

import com.ds9712.ds_9712.ui.intro.IntroActivity
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@IntroScope
@Subcomponent(
    modules = []
)
interface IntroComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): IntroComponent
    }

    @ExperimentalCoroutinesApi
    fun inject(introActivity: IntroActivity)
}