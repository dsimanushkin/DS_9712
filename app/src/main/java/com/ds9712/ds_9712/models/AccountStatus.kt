package com.ds9712.ds_9712.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountStatus(
    @Json(name = "agreement_accepted")
    val agreementAccepted: Boolean,

    @Json(name = "email_confirmed")
    val emailConfirmed: Boolean
): Parcelable