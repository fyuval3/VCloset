//package com.androidfinalproject.utils
//
//import com.androidfinalproject.data.weather_service.WeatherInfo
//import retrofit2.Call
//import retrofit2.Response
//
//abstract class BaseDataSource {
//
//    protected suspend fun <T>
//            getResult(call: () -> Call<WeatherInfo>) : Resource<T> {
//
//        try {
//            val result  = call()
//            if(result.isSuccessful) {
//                val body = result.body()
//                if(body != null) return  Resource.success(body)
//            }
//            return Resource.error("Network call has failed for the following reason: " +
//                    "${result.message()} ${result.code()}")
//        }catch (e : Exception) {
//            return Resource.error("Network call has failed for the following reason: "
//                    + (e.localizedMessage ?: e.toString()))
//        }
//    }
//}