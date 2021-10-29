package com.ds9712.ds_9712.ui

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.BuildConfig
import com.afollestad.materialdialogs.MaterialDialog
import com.ds9712.ds_9712.BaseApplication
import com.ds9712.ds_9712.session.SessionManager
import com.ds9712.ds_9712.util.Response
import com.ds9712.ds_9712.util.StateMessageCallback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), UICommunicationListener {
    private var dialogInView: MaterialDialog? = null

    @Inject
    lateinit var sessionManager: SessionManager

    abstract fun inject()

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BaseApplication).appComponent
            .inject(this)
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {
//        when(response.uiComponentType) {
//            is UIComponentType.None -> {
//                Timber.d("onResponseReceived: ${response.message}")
//                stateMessageCallback.removeMessageFromStack()
//            }
//        }
    }
}