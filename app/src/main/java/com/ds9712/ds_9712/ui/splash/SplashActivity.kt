package com.ds9712.ds_9712.ui.splash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ds9712.ds_9712.BaseApplication
import com.ds9712.ds_9712.ui.BaseActivity
import com.ds9712.ds_9712.ui.auth.AuthActivity
import com.ds9712.ds_9712.ui.intro.IntroActivity
import com.ds9712.ds_9712.ui.main.MainActivity
import com.ds9712.ds_9712.ui.splash.state.SplashStateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class SplashActivity: BaseActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    private var isUserLoggedIn: Boolean = false
    private var showIntro: Boolean = true

    val viewModel: SplashViewModel by viewModels {
        providerFactory
    }

    override fun inject() {
        (application as BaseApplication).splashComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)

        getSharedPrefs()
        subscribeObservers()
        performScreenCheck()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(this, Observer { viewState ->
            viewState.authToken?.let {
                sessionManager.login(it)
            }
        })

        sessionManager.cachedToken.observe(this, Observer { token ->
            token.let { authToken ->
                if (authToken != null && authToken.accountId != "" && authToken.authToken != null) {
                    if (showIntro) {
                        navigateIntroActivity()
                    } else {
                        navigateMainActivity()
                    }
                }
            }
        })
    }

    private fun verifyAccountStatus() {
        viewModel.setStateEvent(SplashStateEvent.CheckPreviousAuthEvent)
    }

    private fun getSharedPrefs() {
        isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false)
        showIntro = sharedPreferences.getBoolean("showIntro", true)
    }

    private fun performScreenCheck() {
        if (isUserLoggedIn) {
            verifyAccountStatus()
        } else {
            navigateAuthActivity()
        }
    }

    private fun navigateAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
        (application as BaseApplication).releaseSplashComponent()
    }

    private fun navigateMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        (application as BaseApplication).releaseSplashComponent()
    }

    private fun navigateIntroActivity() {
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
        (application as BaseApplication).releaseSplashComponent()
    }

    // Not in use
    override fun displaySnackBarMessage(message: String) {}

    // Not in use
    override fun isStoragePermissionGranted(): Boolean {return false}

    // Not in use
    override fun showErrorMessage(message: String) {}
    override fun showProgressBar(isVisible: Boolean) {

    }
}