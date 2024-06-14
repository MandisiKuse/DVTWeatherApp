package com.dvt.weatherapp.domain.mapper

import com.dvt.weatherapp.data.model.CurrentWeather
import com.dvt.weatherapp.data.model.Forecast
import com.dvt.weatherapp.data.model.WeatherData
import com.dvt.weatherapp.ui.statemodel.CurrentWeatherState
import com.dvt.weatherapp.ui.statemodel.Description
import com.dvt.weatherapp.ui.statemodel.ForecastState
import com.dvt.weatherapp.ui.statemodel.WeeklyForecast
import com.dvt.weatherapp.util.Resource
import com.dvt.weatherapp.util.getDayFromDate

class WeatherMapper {
    fun mapWeatherToWeatherState(response: Resource<CurrentWeather>) =
        when (response) {
            is Resource.Success -> {
                response.data?.let { weatherData ->
                    Resource.Success(
                        CurrentWeatherState(
                            current = weatherData.main?.temp?.toInt(),
                            min = weatherData.main?.temp_min?.toInt(),
                            max = weatherData.main?.temp_max?.toInt(),
                            description = Description.valueOf(
                                weatherData.weather?.main?.uppercase() ?: "CLEAR"
                            )
                        )
                    )
                } ?: Resource.Error(response.message)
            }

            is Resource.Error -> Resource.Error(response.message)

            is Resource.Loading -> Resource.Loading()
        }

    fun mapForecastToForecastState(response: Resource<Forecast>) =
        when (response) {
            is Resource.Success -> {
                response.data?.list.let { forecastList ->
                    Resource.Success(
                        ForecastState(
                            forecast = forecastData(forecastList)
                        )
                    )
                }
            }

            is Resource.Error -> Resource.Error(response.message)

            is Resource.Loading -> Resource.Loading()
        }

    private fun forecastData(forecast: List<WeatherData>?): List<WeeklyForecast> {
        val weatherList = mutableListOf<WeatherData>()

        forecast?.forEach { weatherData ->
            weatherList.add(weatherData)
        }
        return weatherList.map {
            val date = it.dt_txt?.substringBefore(" ").toString()
            WeeklyForecast(
                day = getDayFromDate(date),
                temp = it.main?.temp?.toInt(),
                description = Description.valueOf(
                    it.weather?.first()?.main?.uppercase() ?: "CLEAR"
                )
            )
        }
    }
}