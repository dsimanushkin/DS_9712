package com.ds9712.ds_9712.api.auth.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SignUpResponse(
    @Json(name = "status")
    var status: String,

    @Json(name = "status_code")
    var statusCode: Int,

    @Json(name = "status_message")
    var statusMessage: String,

    @Json(name = "id")
    var id: String,

    @Json(name = "username")
    var username: String,

    @Json(name = "short_id")
    var shortId: String,

    @Json(name = "auth_token")
    var authToken: String
) {
    override fun toString(): String {
        return "SignUpResponse(status='$status', statusCode=$statusCode, statusMessage='$statusMessage', id='$id', username='$username', shortId='$shortId', authToken='$authToken')"
    }
}