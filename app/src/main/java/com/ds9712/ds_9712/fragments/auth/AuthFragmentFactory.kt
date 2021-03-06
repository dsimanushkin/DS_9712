package com.ds9712.ds_9712.fragments.auth

import android.content.SharedPreferences
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.ui.auth.*
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
            LoginFragment::class.java.name -> {
                LoginFragment(viewModelFactory, sharedPreferences, sharedPrefsEditor)
            }
            SignUpStepOneFragment::class.java.name -> {
                SignUpStepOneFragment(viewModelFactory)
            }
            SignUpStepTwoFragment::class.java.name -> {
                SignUpStepTwoFragment(viewModelFactory)
            }
            SignUpStepThreeFragment::class.java.name -> {
                SignUpStepThreeFragment(viewModelFactory)
            }
            SignUpStepFourFragment::class.java.name -> {
                SignUpStepFourFragment(viewModelFactory)
            }
            AgreementFragment::class.java.name -> {
                AgreementFragment(viewModelFactory)
            }
            TermsOfUseFragment::class.java.name -> {
                TermsOfUseFragment(viewModelFactory)
            }
            PrivacyPolicyFragment::class.java.name -> {
                PrivacyPolicyFragment(viewModelFactory)
            }
            VerifyEmailFragment::class.java.name -> {
                VerifyEmailFragment(viewModelFactory, sharedPreferences, sharedPrefsEditor)
            }
            ChangeEmailAddressFragment::class.java.name -> {
                ChangeEmailAddressFragment(viewModelFactory)
            }
            else -> {
                LauncherFragment(viewModelFactory, sharedPreferences)
            }
        }
}