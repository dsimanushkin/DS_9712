package com.ds9712.ds_9712.ui.intro

import android.content.SharedPreferences
import android.os.Bundle
import com.ds9712.ds_9712.BaseApplication
import com.ds9712.ds_9712.ui.BaseActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class IntroActivity: BaseActivity() {
    @Inject
    lateinit var sharedPrefsEditor: SharedPreferences.Editor

//    private lateinit var binding: ActivityIntroBinding

    override fun inject() {
        (application as BaseApplication).introComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }

    override fun isStoragePermissionGranted(): Boolean {
        return false
    }

    override fun displaySnackBarMessage(message: String) {}
    override fun showErrorMessage(message: String) {}
    override fun showProgressBar(isVisible: Boolean) {}

}