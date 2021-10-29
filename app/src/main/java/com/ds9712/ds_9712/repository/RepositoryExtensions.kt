package com.ds9712.ds_9712.repository

import com.ds9712.ds_9712.util.*
import com.ds9712.ds_9712.util.Constants.Companion.CACHE_TIMEOUT
import com.ds9712.ds_9712.util.Constants.Companion.NETWORK_TIMEOUT
import com.ds9712.ds_9712.util.ErrorHandling.Companion.CACHE_ERROR_TIMEOUT
import com.ds9712.ds_9712.util.ErrorHandling.Companion.NETWORK_ERROR_TIMEOUT
import com.ds9712.ds_9712.util.ErrorHandling.Companion.UNKNOWN_ERROR
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(NETWORK_TIMEOUT){
                ApiResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
                }
                is IOException -> {
                    ApiResult.NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    ApiResult.GenericError(
                        code,
                        errorResponse
                    )
                }
                else -> {
                    ApiResult.GenericError(
                        null,
                        UNKNOWN_ERROR
                    )
                }
            }
        }
    }
}

suspend fun <T> safeFileApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            ApiResult.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is IOException -> {
                    ApiResult.NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    ApiResult.GenericError(
                        code,
                        errorResponse
                    )
                }
                else -> {
                    ApiResult.GenericError(
                        null,
                        UNKNOWN_ERROR
                    )
                }
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T?
): CacheResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(CACHE_TIMEOUT){
                CacheResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is TimeoutCancellationException -> {
                    CacheResult.GenericError(CACHE_ERROR_TIMEOUT)
                }
                else -> {
                    CacheResult.GenericError(UNKNOWN_ERROR)
                }
            }
        }
    }
}


fun <ViewState> buildError(
    message: String,
    statusCode: Int,
    requestCode: Int,
    stateEvent: StateEvent?
): DataState<ViewState> {
    return DataState.error(
        response = Response(
            message = message,
            statusCode = statusCode,
            requestCode = requestCode
        ),
        stateEvent = stateEvent
    )
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        UNKNOWN_ERROR
    }
}