package com.ds9712.ds_9712.ui

import android.app.Activity
import android.widget.Toast
import com.ds9712.ds_9712.util.StateMessageCallback

fun Activity.displayToast(
    message: String,
    stateMessageCallback: StateMessageCallback
) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    stateMessageCallback.removeMessageFromStack()
}

interface AreYouSureCallback {
    fun proceed()
    fun cancel()
}