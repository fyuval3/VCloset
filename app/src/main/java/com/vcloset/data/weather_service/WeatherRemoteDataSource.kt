package com.vcloset.data.weather_service

import com.vcloset.data.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRemoteDataSource @Inject constructor(private val weatherService: WeatherService) :
    BaseDataSource() {
    suspend fun getTemp(latitude: Double, longitude: Double) =
        getResult { weatherService.getTemp(latitude, longitude, Constants.APP_ID, Constants.UNITS) }
}