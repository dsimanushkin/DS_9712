package com.ds9712.ds_9712.api.auth.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LoginResponse(
    @Json(name = "status")
    var status: String,

    @Json(name = "status_code")
    var statusCode: Int,

    @Json(name = "status_message")
    var statusMessage: String,

    @Json(name = "id")
    var id: String?,

    @Json(name = "short_id")
    var shortId: String?,

    @Json(name = "full_name")
    var fullName: String?,

    @Json(name = "username")
    var username: String?,

    @Json(name = "email")
    var email: String?,

    @Json(name = "profile_picture")
    var profilePicture: String?,

    @Json(name = "created_at")
    var createdAt: String?,

    @Json(name = "updated_at")
    var updatedAt: String?,

    @Json(name = "is_agreement_accepted")
    var isAgreementAccepted: Boolean?,

    @Json(name = "is_email_confirmed")
    var isEmailConfirmed: Boolean?,

    @Json(name = "auth_token")
    var authToken: String?
) {
    override fun toString(): String {
        return "LoginResponse(status='$status', statusCode=$statusCode, statusMessage='$statusMessage', id=$id, shortId='$shortId', fullName=$fullName, username=$username, email=$email, profilePicture=$profilePicture, createdAt=$createdAt, updatedAt=$updatedAt, isAgreementAccepted=$isAgreementAccepted, isEmailConfirmed=$isEmailConfirmed, authToken=$authToken)"
    }
}