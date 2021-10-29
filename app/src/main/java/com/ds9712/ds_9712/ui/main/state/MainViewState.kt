package com.ds9712.ds_9712.ui.main.state

import android.app.Application
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainViewState(
    var placegolderFields: PlaceholderFields? = null
): Parcelable

@Parcelize
data class PlaceholderFields(
    var loginUsername: String? = null,
    var loginPassword: String? = null
): Parcelable {
    class LoginError {

    }

    fun isValidForSubmission(application: Application): String {
        return ""
    }
}