package com.dvt.weatherapp.domain.model

data class Forecast(
    val city: City?,
    val cod: String?,
    val message: Int?,
    val cnt: Int?,
    val list: List<WeatherData>?
)
