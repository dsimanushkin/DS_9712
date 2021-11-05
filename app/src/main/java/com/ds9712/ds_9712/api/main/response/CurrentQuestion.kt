package com.ds9712.ds_9712.api.main.response

import com.ds9712.ds_9712.models.CurrentQuestion
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CurrentQuestion(
    @Json(name = "id")
    val id: String,

    @Json(name = "q_id")
    val qId: String,

    @Json(name = "q_title")
    val qTitle: String,

    @Json(name = "q_image")
    val qImage: String
) {
    fun toCurrentQuestion(): CurrentQuestion {
        return CurrentQuestion(
            id = id,
            qId = qId,
            qTitle = qTitle,
            qImage = qImage
        )
    }

    override fun toString(): String {
        return "CurrentQuestion(id='$id', qId='$qId', qTitle='$qTitle', qImage='$qImage')"
    }
}