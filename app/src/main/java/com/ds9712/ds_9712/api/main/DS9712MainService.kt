package com.ds9712.ds_9712.api.main

import com.ds9712.ds_9712.api.GenericResponse
import com.ds9712.ds_9712.api.main.response.CurrentQuestionResponse
import com.ds9712.ds_9712.di.main.MainScope
import com.ds9712.ds_9712.util.Constants
import retrofit2.http.*

@MainScope
interface DS9712MainService {
    @GET("question/current")
    suspend fun getCurrentQuestion(
        @Header("api_token") apiToken: String? = Constants.API_TOKEN,
        @Header("auth_token") authToken: String
    ): CurrentQuestionResponse

    @POST("question/solve")
    @FormUrlEncoded
    suspend fun solveQuestion(
        @Header("api_token") apiToken: String? = Constants.API_TOKEN,
        @Header("auth_token") authToken: String,
        @Field("user_key") userKey: String
    ): GenericResponse
}