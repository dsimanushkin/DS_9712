package com.ds9712.ds_9712.fragments.auth

import android.content.SharedPreferences
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.ui.auth.LauncherFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@AuthScope
class AuthFragmentFactory
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
            LauncherFragment::class.java.name -> {
                LauncherFragment(viewModelFactory, sharedPreferences)
            }
            else -> {
                LauncherFragment(viewModelFactory, sharedPreferences)
            }
        }
}