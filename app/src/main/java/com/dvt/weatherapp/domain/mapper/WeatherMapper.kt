package com.dvt.weatherapp.domain.mapper

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import com.dvt.weatherapp.data.remote.model.Forecast
import com.dvt.weatherapp.data.remote.model.WeatherData
import com.dvt.weatherapp.ui.statemodel.CurrentWeatherState
import com.dvt.weatherapp.ui.statemodel.Description
import com.dvt.weatherapp.ui.statemodel.WeeklyForecast
import com.dvt.weatherapp.util.Resource
import com.dvt.weatherapp.util.getDayFromDate

class WeatherMapper {
    fun mapWeatherToWeatherState(response: Resource<Forecast>) =
        when (response) {
            is Resource.Success -> {
                response.data?.list.let { forecastList ->
                    Resource.Success(
                        CurrentWeatherState(
                            current = forecastList?.first()?.main?.temp?.toInt() ?: 0,
                            min = forecastList?.first()?.main?.temp_min?.toInt() ?: 0,
                            max = forecastList?.first()?.main?.temp_max?.toInt() ?: 0,
                            description = Description.valueOf(
                                forecastList?.first()?.weather?.first()?.main?.toUpperCase(
                                    Locale.current
                                ) ?: "CLEAR"
                            ),
                            forecast = forecastData(forecastList)
                        )
                    )
                }
            }

            is Resource.Error -> Resource.Error(response.message)

            is Resource.Loading -> Resource.Loading()
        }

    private fun forecastData(forecast: List<WeatherData>?): List<WeeklyForecast> {
        val dayOfWeek = mutableSetOf<String>()
        val weatherList = mutableListOf<WeatherData>()

        forecast?.forEach { weatherData ->
            weatherData.dt_txt?.substringBefore(" ")?.let { day ->
                if (day !in dayOfWeek) {
                    dayOfWeek.add(day)
                    weatherList.add(weatherData)
                }
            }
        }

        if (weatherList.isNotEmpty()) {
            weatherList.removeFirst()
        }

        return weatherList.map {
            val date = it.dt_txt?.substringBefore(" ").toString()
            WeeklyForecast(
                day = getDayFromDate(date),
                temp = it.main?.temp?.toInt() ?: 0,
                description = Description.valueOf(
                    it.weather?.first()?.main?.uppercase() ?: "CLEAR"
                )
            )
        }
    }
}