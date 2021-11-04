package com.ds9712.ds_9712.api.auth.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CurrentQuestionResponse(
    @Json(name = "status")
    var status: String,

    @Json(name = "status_code")
    var statusCode: Int,

    @Json(name = "status_message")
    var statusMessage: String,

    @Json(name = "question")
    var question: CurrentQuestion? = null
) {
    override fun toString(): String {
        return "CurrentQuestionResponse(status='$status', statusCode=$statusCode, statusMessage='$statusMessage', question=$question)"
    }

    fun toCurrentQuestion(): com.ds9712.ds_9712.models.CurrentQuestion {
        return this.question!!.toCurrentQuestion()
    }
}