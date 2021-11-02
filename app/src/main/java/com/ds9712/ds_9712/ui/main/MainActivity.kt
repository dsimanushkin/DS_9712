package com.ds9712.ds_9712.ui.main

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.ds9712.ds_9712.BaseApplication
import com.ds9712.ds_9712.R
import com.ds9712.ds_9712.databinding.ActivityAuthBinding
import com.ds9712.ds_9712.databinding.ActivityMainBinding
import com.ds9712.ds_9712.fragments.auth.AuthNavHostFragment
import com.ds9712.ds_9712.fragments.main.MainNavHostFragment
import com.ds9712.ds_9712.ui.BaseActivity
import com.ds9712.ds_9712.ui.auth.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity: BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun inject() {
        (application as BaseApplication).mainComponent().inject(this)
    }

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    val viewModel: MainViewModel by viewModels {
        providerFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onRestoreInstanceState()
    }

    private fun onRestoreInstanceState() {
        val host = supportFragmentManager.findFragmentById(binding.mainFragmentContainer.id)
        host?.let {
            // Do nothing here
        }?: createNavHost()
    }

    private fun createNavHost() {
        val navHost = MainNavHostFragment.create(
            R.navigation.nav_main
        )
        supportFragmentManager.beginTransaction()
            .replace(binding.mainFragmentContainer.id, navHost, getString(R.string.MainNavHost))
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

    override fun isStoragePermissionGranted(): Boolean = false
    override fun showErrorMessage(message: String) {}
    override fun showProgressBar(isVisible: Boolean) {}
}