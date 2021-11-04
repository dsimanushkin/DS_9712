package com.ds9712.ds_9712.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentQuestion(
    @Json(name = "id")
    val id: String,

    @Json(name = "q_id")
    val qId: String,

    @Json(name = "q_title")
    val qTitle: String,

    @Json(name = "q_image")
    val qImage: String
): Parcelable