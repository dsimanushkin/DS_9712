package com.ds9712.ds_9712.ui.main

import android.os.Bundle
import androidx.fragment.app.FragmentFactory
import com.ds9712.ds_9712.BaseApplication
import com.ds9712.ds_9712.ui.BaseActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity: BaseActivity() {

    override fun inject() {
        (application as BaseApplication).mainComponent().inject(this)
    }

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }

    override fun isStoragePermissionGranted(): Boolean = false

    override fun displaySnackBarMessage(message: String) {}
    override fun showErrorMessage(message: String) {}
    override fun showProgressBar(isVisible: Boolean) {}
}