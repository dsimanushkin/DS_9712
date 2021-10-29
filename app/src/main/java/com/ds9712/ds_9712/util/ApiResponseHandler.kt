package com.ds9712.ds_9712.util

import com.ds9712.ds_9712.util.ErrorHandling.Companion.NETWORK_ERROR

abstract class ApiResponseHandler<ViewState, Data>(
    private val response: ApiResult<Data?>,
    private val stateEvent: StateEvent
) {

    suspend fun getResult(): DataState<ViewState>{
        return when(response){
            is ApiResult.GenericError -> {
                DataState.error(
                    response = Response(
                        message = response.errorMessage.toString(),
                        statusCode = -2,
                        requestCode = -1
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.NetworkError -> {
                DataState.error(
                    response = Response(
                        message = NETWORK_ERROR,
                        statusCode = -2,
                        requestCode = -1
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.Success -> {
                if(response.value == null){
                    DataState.error(
                        response = Response(
                            message = "Data is NULL.",
                            statusCode = -2,
                            requestCode = -1
                        ),
                        stateEvent = stateEvent
                    )
                }
                else{
                    handleSuccess(resultObj = response.value)
                }
            }
        }
    }
    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>
}