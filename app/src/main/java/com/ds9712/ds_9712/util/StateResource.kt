package com.ds9712.ds_9712.util

data class StateMessage(val response: Response)

data class Response(
    val message: String?,
    val statusCode: Int?,
    val requestCode: Int?
)

interface StateMessageCallback{
    fun removeMessageFromStack()
}