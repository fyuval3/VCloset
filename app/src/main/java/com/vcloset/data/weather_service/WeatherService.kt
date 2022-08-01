package com.vcloset.data.weather_service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather?")
    suspend fun getTemp(@Query("lat") latitude: Double, @Query("lon") longitude: Double, @Query("appid") appId: String, @Query("units") units: String) : Response<WeatherInfo>
}