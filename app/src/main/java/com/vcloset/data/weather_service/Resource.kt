package com.vcloset.data.weather_service

class Resource<out T> private constructor(val status: Status<T>) {
    companion object {
        fun <T> success(data: T) = Resource(Success(data))
        fun <T> error(message: String, data: T? = null) = Resource(Error(message, data))
    }
}

sealed class Status<out T>(val data: T? = null)

class Success<T>(data: T) : Status<T>(data)
class Error<T>(val message: String, data: T? = null) : Status<T>(data)