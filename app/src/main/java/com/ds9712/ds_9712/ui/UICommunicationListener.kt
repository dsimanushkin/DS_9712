package com.ds9712.ds_9712.ui

import com.ds9712.ds_9712.util.Response
import com.ds9712.ds_9712.util.StateMessageCallback

interface UICommunicationListener {

    fun hideSoftKeyboard()

    fun isStoragePermissionGranted(): Boolean

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

    fun displaySnackBarMessage(message: String)

    fun showErrorMessage(message: String)

    fun showProgressBar(isVisible: Boolean)
}