package com.vcloset.data.weather_service

data class Main(val temp : Double)

data class WeatherCodeInfo(val icon : String)

data class WeatherInfo (val main: Main, val weather: List<WeatherCodeInfo>)