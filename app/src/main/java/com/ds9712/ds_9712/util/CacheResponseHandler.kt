package com.ds9712.ds_9712.util

abstract class CacheResponseHandler<ViewState, Data>(
    private val response: CacheResult<Data?>,
    private val stateEvent: StateEvent?
) {
    suspend fun getResult(): DataState<ViewState>{
        return when(response){

            is CacheResult.GenericError -> {
                DataState.error(
                    response = Response(
                        message = response.errorMessage,
                        statusCode = -2,
                        requestCode = -1
                    ),
                    stateEvent = stateEvent
                )
            }

            is CacheResult.Success -> {
                if(response.value == null){
                    DataState.error(
                        response = Response(
                            message = "Reason: Data is NULL.",
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