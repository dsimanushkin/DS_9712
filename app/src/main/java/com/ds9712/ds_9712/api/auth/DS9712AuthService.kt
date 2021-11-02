package com.ds9712.ds_9712.api.auth

import com.ds9712.ds_9712.api.GenericResponse
import com.ds9712.ds_9712.api.auth.response.AccountStatusResponse
import com.ds9712.ds_9712.api.auth.response.LoginResponse
import com.ds9712.ds_9712.api.auth.response.SignUpResponse
import com.ds9712.ds_9712.di.auth.AuthScope
import com.ds9712.ds_9712.util.Constants.Companion.API_TOKEN
import retrofit2.http.*

@AuthScope
interface DS9712AuthService {
    @GET("account/signup/status")
    suspend fun accountStatus(
        @Header("api_token") apiToken: String? = API_TOKEN,
        @Header("auth_token") authToken: String
    ): AccountStatusResponse

    @POST("account/login")
    @FormUrlEncoded
    suspend fun login(
        @Header("api_token") apiToken: String? = API_TOKEN,
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("account/signup/p1/validation")
    @FormUrlEncoded
    suspend fun signUpStepOneFieldsValidation(
        @Header("api_token") apiToken: String? = API_TOKEN,
        @Field("full_name") fullName: String,
        @Field("username") username: String
    ): GenericResponse

    @POST("account/signup/p2/validation")
    @FormUrlEncoded
    suspend fun signUpStepTwoFieldsValidation(
        @Header("api_token") apiToken: String? = API_TOKEN,
        @Field("email") email: String
    ): GenericResponse

    @POST("account/signup/p3/validation")
    @FormUrlEncoded
    suspend fun signUpStepThreeFieldsValidation(
        @Header("api_token") apiToken: String? = API_TOKEN,
        @Field("password") password: String
    ): GenericResponse

    @POST("account/signup")
    @FormUrlEncoded
    suspend fun signUp(
        @Header("api_token") apiToken: String? = API_TOKEN,
        @Field("full_name") fullName: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("date_of_birth") dateOfBirth: String
    ): SignUpResponse

    @POST("account/agreement/accept")
    suspend fun acceptAgreement(
        @Header("api_token") apiToken: String? = API_TOKEN,
        @Header("auth_token") authToken: String
    ): GenericResponse

    @DELETE("account/agreement/decline")
    suspend fun declineAgreement(
        @Header("api_token") apiToken: String? = API_TOKEN,
        @Header("auth_token") authToken: String
    ): GenericResponse

    @POST("account/signup/email/verify")
    @FormUrlEncoded
    suspend fun verifyEmail(
        @Header("api_token") apiToken: String? = API_TOKEN,
        @Header("auth_token") authToken: String,
        @Field("ver_code") verificationCode: String
    ): GenericResponse

    @POST("account/ver_code/resend")
    suspend fun resendVerificationCode(
        @Header("api_token") apiToken: String? = API_TOKEN,
        @Header("auth_token") authToken: String,
    ): GenericResponse

    @POST("account/signup/email/change")
    @FormUrlEncoded
    suspend fun changeEmailAddress(
        @Header("api_token") apiToken: String? = API_TOKEN,
        @Header("auth_token") authToken: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): GenericResponse
}