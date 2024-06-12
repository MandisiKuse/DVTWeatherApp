package com.dvt.weatherapp.domain.model

data class WeatherData(
    val dt: Int?,
    val main: Main?,
    val weather: List<Weather>?,
    val clouds: Clouds?,
    val wind: Wind?,
    val visibility: Int?,
    val pop: Double?,
    val rain: Rain?,
    val sys: Sys?,
    val dt_txt: String?
)
