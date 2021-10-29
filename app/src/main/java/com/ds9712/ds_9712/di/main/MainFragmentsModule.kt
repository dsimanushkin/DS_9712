package com.ds9712.ds_9712.di.main

import android.content.SharedPreferences
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.ds9712.ds_9712.fragments.main.MainFragmentFactory
import dagger.Module
import dagger.Provides

@Module
object MainFragmentsModule {
    @JvmStatic
    @MainScope
    @Provides
    fun provideFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory,
        sharedPreferences: SharedPreferences,
        sharedPrefsEditor: SharedPreferences.Editor
    ): FragmentFactory {
        return MainFragmentFactory(
            viewModelFactory,
            sharedPreferences,
            sharedPrefsEditor
        )
    }
}