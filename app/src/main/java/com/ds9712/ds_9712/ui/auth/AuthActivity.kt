package com.ds9712.ds_9712.ui.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ds9712.ds_9712.BaseApplication
import com.ds9712.ds_9712.R
import com.ds9712.ds_9712.databinding.ActivityAuthBinding
import com.ds9712.ds_9712.fragments.auth.AuthNavHostFragment
import com.ds9712.ds_9712.ui.BaseActivity
import com.ds9712.ds_9712.ui.auth.state.AuthStateEvent
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class AuthActivity: BaseActivity() {
    private lateinit var binding: ActivityAuthBinding

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var prefsEditor: SharedPreferences.Editor

    val viewModel: AuthViewModel by viewModels {
        providerFactory
    }

    override fun inject() {
        (application as BaseApplication).authComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeObservers()
        onRestoreInstanceState()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(this, Observer {viewState ->
            viewState.authToken?.let {
                sessionManager.login(it)
            }
        })

        sessionManager.cachedToken.observe(this, Observer { token ->
            token.let { authToken ->
                if (authToken != null && authToken.accountId != "" && authToken.authToken != null) {
                    verifyAccountStatus()
                }
            }
        })
    }

    private fun verifyAccountStatus() {
        viewModel.setStateEvent(AuthStateEvent.AccountStatusEvent)
    }

    private fun onRestoreInstanceState() {
        val host = supportFragmentManager.findFragmentById(binding.authFragmentContainer.id)
        host?.let {
            // Do nothing here
        }?: createNavHost()
    }

    private fun createNavHost() {
        val navHost = AuthNavHostFragment.create(
            R.navigation.nav_authentication
        )
        supportFragmentManager.beginTransaction()
            .replace(binding.authFragmentContainer.id, navHost, getString(R.string.AuthNavHost))
            .setPrimaryNavigationFragment(navHost)
            .commit()
    }

    override fun displaySnackBarMessage(message: String) {
        val snackbar = Snackbar.make(binding.fragmentContainer, message, Snackbar.LENGTH_SHORT)
        val view2 = snackbar.view
        snackbar.view.setBackgroundColor(getColor(R.color.white))
        val textView = view2.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(getColor(R.color.dark_text_color))
        val typeface = ResourcesCompat.getFont(this, R.font.vt323_regular)
        textView.typeface = typeface
        snackbar.show()
    }

    private fun checkPreviousAuthUser() {
        viewModel.setStateEvent(AuthStateEvent.CheckPreviousAuthEvent)
    }

    override fun onResume() {
        super.onResume()
        checkPreviousAuthUser()
    }

    override fun showErrorMessage(message: String) {}
    override fun showProgressBar(isVisible: Boolean) {}

    override fun isStoragePermissionGranted(): Boolean {
        return false
    }
}