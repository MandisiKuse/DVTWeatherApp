package com.dvt.weatherapp.data.remote

import com.dvt.weatherapp.data.remote.model.CurrentWeather
import com.dvt.weatherapp.data.remote.model.Forecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    companion object {
        const val API_KEY = ""
        const val UNIT = "metric"
    }

    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = UNIT
    ): Response<CurrentWeather>

    @GET("data/2.5/forecast")
    suspend fun getForecastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = UNIT
    ): Response<Forecast>
}