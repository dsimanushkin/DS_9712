package com.ds9712.ds_9712.ui.splash.state

import android.os.Parcelable
import com.ds9712.ds_9712.models.AuthToken
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SplashViewState(
    var authToken: AuthToken? = null,
): Parcelable