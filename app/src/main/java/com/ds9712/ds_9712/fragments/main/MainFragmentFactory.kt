package com.ds9712.ds_9712.fragments.main

import android.content.SharedPreferences
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.ds9712.ds_9712.di.main.MainScope
import javax.inject.Inject

@MainScope
class MainFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val sharedPreferences: SharedPreferences,
    private val sharedPrefsEditor: SharedPreferences.Editor
): FragmentFactory() {

//    @ExperimentalCoroutinesApi
//    @FlowPreview
//    override fun instantiate(classLoader: ClassLoader, className: String) =
//        when(className) {
//
//        }
}