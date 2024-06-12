package com.dvt.weatherapp.data.model

data class Forecast(
    val city: City?,
    val cod: String?,
    val message: Int?,
    val cnt: Int?,
    val list: List<WeatherData>?
)
