package com.dvt.weatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvt.weatherapp.domain.mapper.CityWeatherMapper
import com.dvt.weatherapp.domain.mapper.WeatherMapper
import com.dvt.weatherapp.domain.repository.CurrentLocationRepository
import com.dvt.weatherapp.domain.repository.WeatherRepository
import com.dvt.weatherapp.ui.statemodel.CurrentWeatherState
import com.dvt.weatherapp.ui.statemodel.WeatherByCityState
import com.dvt.weatherapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: CurrentLocationRepository,
    private val weatherMapper: WeatherMapper,
    private val cityWeatherMapper: CityWeatherMapper
) : ViewModel() {

    //Properties Section
    private val _weatherStateFlow = MutableStateFlow<Resource<CurrentWeatherState>?>(Resource.Loading())
    val weatherStateFlow = _weatherStateFlow.asStateFlow()

    private val _weatherByCityStateFlow = MutableStateFlow<Resource<WeatherByCityState>?>(Resource.Loading())
    val weatherByCityStateFlow = _weatherByCityStateFlow.asStateFlow()
    //end of section


    //Public API's Section
    fun getCurrentLocation() = viewModelScope.launch {
        locationRepository.getCurrentLocation().let { location ->
            getCurrentForecast(location.latitude, location.longitude)
        }
    }

    fun getCityWeather(city: String) = viewModelScope.launch {
        getCurrentWeatherByCity(city)
    }
    //end of section


    //Private Functions Section
    private fun getCurrentForecast(lat: Double, lon: Double) = viewModelScope.launch {
        getForecast(lat, lon).collect { forecast ->
            _weatherStateFlow.value = forecast
        }
    }

    private suspend fun getForecast(lat: Double, lon: Double) =
        weatherRepository.getForecast(lat, lon).map { forecast ->
            weatherMapper.mapWeatherToWeatherState(forecast)
        }

    private fun getCurrentWeatherByCity(city: String) = viewModelScope.launch {
        getWeatherByCity(city).collect { weather ->
            _weatherByCityStateFlow.value = weather
        }
    }

    private suspend fun getWeatherByCity(city: String) =
        weatherRepository.getWeatherByCity(city).map { weather ->
            cityWeatherMapper.mapCityWeatherToWeatherState(weather)
        }
    //end of section
}