package com.dvt.weatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvt.weatherapp.domain.mapper.WeatherMapper
import com.dvt.weatherapp.domain.repository.CurrentLocationRepository
import com.dvt.weatherapp.domain.repository.WeatherRepository
import com.dvt.weatherapp.ui.statemodel.CurrentWeatherState
import com.dvt.weatherapp.ui.statemodel.ForecastState
import com.dvt.weatherapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: CurrentLocationRepository,
    private val weatherMapper: WeatherMapper
) : ViewModel() {

    //Properties Section
    private val _weatherStateFlow = MutableStateFlow<Resource<CurrentWeatherState>?>(null)
    val weatherStateFlow = _weatherStateFlow.asStateFlow()

    private val _forecastStateFlow = MutableStateFlow<Resource<ForecastState>?>(null)
    val forecastStateFlow = _forecastStateFlow.asStateFlow()
    //end of section


    //Public API's Section
    fun getCurrentLocation() = viewModelScope.launch {
        locationRepository.getCurrentLocation().let { location ->
            getCurrentWeather(location.latitude, location.longitude)
        }

        locationRepository.getCurrentLocation().let { location ->
            getCurrentForecast(location.latitude, location.longitude)
        }
    }
    //end of section


    //Private Functions Section
    private suspend fun getLocation() = locationRepository.getCurrentLocation()

    private fun getCurrentWeather(lat: Double, lon: Double) = viewModelScope.launch {
        getWeather(lat, lon).collect { weather ->
            _weatherStateFlow.value = weather
        }
    }

    private suspend fun getWeather(lat: Double, lon: Double) =
        weatherRepository.getWeather(lat, lon).map { weather ->
            weatherMapper.mapWeatherToWeatherState(weather)
        }

    private fun getCurrentForecast(lat: Double, lon: Double) = viewModelScope.launch {
        getForecast(lat, lon).collect { forecast ->
            _forecastStateFlow.value = forecast
        }
    }

    private suspend fun getForecast(lat: Double, lon: Double) =
        weatherRepository.getForecast(lat, lon).map { forecast ->
            weatherMapper.mapForecastToForecastState(forecast)
        }
    //end of section
}