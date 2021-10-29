package com.ds9712.ds_9712.api.auth.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class AccountStatusResponse(
    @Json(name = "status")
    var status: String,

    @Json(name = "status_code")
    var statusCode: Int,

    @Json(name = "status_message")
    var statusMessage: String,

    @Json(name = "is_email_confirmed")
    var isEmailConfirmed: Boolean,

    @Json(name = "is_agreement_accepted")
    var isAgreementAccepted: Boolean
) {
    override fun toString(): String {
        return "AccountStatusResponse(status='$status', statusCode=$statusCode, statusMessage='$statusMessage', isEmailConfirmed=$isEmailConfirmed, isAgreementAccepted=$isAgreementAccepted)"
    }
}