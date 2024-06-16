package com.dvt.weatherapp.domain.mapper

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import com.dvt.weatherapp.data.remote.model.CurrentWeather
import com.dvt.weatherapp.ui.statemodel.Description
import com.dvt.weatherapp.ui.statemodel.WeatherByCityState
import com.dvt.weatherapp.util.Resource

class CityWeatherMapper {

    fun mapCityWeatherToWeatherState(response: Resource<CurrentWeather>) =
        when (response) {
            is Resource.Success -> {
                response.data?.let { weather ->
                    Resource.Success(
                        WeatherByCityState(
                            current = weather.main?.temp?.toInt() ?: 0,
                            min = weather.main?.temp_min?.toInt() ?: 0,
                            max = weather.main?.temp_max?.toInt() ?: 0,
                            description = Description.valueOf(
                                weather.weather?.first()?.main?.toUpperCase(Locale.current) ?: "CLEAR"
                            )
                        )
                    )
                }
            }

            is Resource.Error -> Resource.Error(response.message)

            is Resource.Loading -> Resource.Loading()
        }
}