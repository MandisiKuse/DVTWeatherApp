package com.dvt.weatherapp.domain.repository

import com.dvt.weatherapp.data.model.CurrentWeather
import com.dvt.weatherapp.data.model.Forecast
import com.dvt.weatherapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeather(lat: Double, lon: Double): Flow<Resource<CurrentWeather>>

    suspend fun getForecast(lat: Double, lon: Double): Flow<Resource<Forecast>>
}