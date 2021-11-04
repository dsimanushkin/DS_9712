package com.ds9712.ds_9712.fragments.main

import android.content.SharedPreferences
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.ds9712.ds_9712.di.main.MainScope
import com.ds9712.ds_9712.ui.main.FinalFragment
import com.ds9712.ds_9712.ui.main.MainFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@MainScope
class MainFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val sharedPreferences: SharedPreferences,
    private val sharedPrefsEditor: SharedPreferences.Editor
): FragmentFactory() {

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun instantiate(classLoader: ClassLoader, className: String) =
        when(className) {
            MainFragment::class.java.name -> {
                MainFragment(viewModelFactory)
            }
            FinalFragment::class.java.name -> {
                FinalFragment(viewModelFactory)
            }
            else -> {
                MainFragment(viewModelFactory)
            }
        }
}