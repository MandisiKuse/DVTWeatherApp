package com.dvt.weatherapp.domain.repository

import com.dvt.weatherapp.data.remote.model.CurrentWeather
import com.dvt.weatherapp.data.remote.model.Forecast
import com.dvt.weatherapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeatherByCity(city: String): Flow<Resource<CurrentWeather>>

    suspend fun getForecast(lat: Double, lon: Double): Flow<Resource<Forecast>>
}