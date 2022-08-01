package com.vcloset.data.weather_service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationRequest
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vcloset.R
import com.vcloset.data.AppRepository
import com.vcloset.data.Constants
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: AppRepository) :
    ViewModel() {
    private val _weather = MutableLiveData<Resource<WeatherInfo>>()
    val weather: LiveData<Resource<WeatherInfo>> = _weather

    fun getColdOrWarm(): String {
        return if (weather.value != null && weather.value!!.status.data != null) {
            val temp = weather.value!!.status.data!!.main.temp.toInt()
            if (temp < Constants.MIN_WARM_WEATHER) Constants.COLD else Constants.WARM
        } else {
            Constants.NO_WEATHER
        }
    }

    fun fetchWeather(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.getCurrentLocation(
                LocationRequest.QUALITY_LOW_POWER,
                CancellationTokenSource().token
            ).addOnSuccessListener {
                viewModelScope.launch(Dispatchers.IO) {
                    if (it != null) {
                        val weatherInfoResource = repository.getTemp(it.latitude, it.longitude)
                        weatherInfoResource.let {
                            _weather.postValue(it)
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(
                    context,
                    R.string.getLocationFailed,
                    Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}