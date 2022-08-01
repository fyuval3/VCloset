package com.vcloset.data.weather_service

import retrofit2.Response
import java.lang.Exception

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val result = call()
            if (result.isSuccessful) {
                val body = result.body()
                if (body != null) {
                    return Resource.success(body)
                }
            }
            return Resource.error("Network call has failed due to the following reason: " + "${result.message()} ${result.code()}")
        } catch (ex: Exception) {
            return Resource.error(
                "Network call has failed due to the following reason: " + (ex.localizedMessage
                    ?: ex.toString())
            )
        }
    }
}