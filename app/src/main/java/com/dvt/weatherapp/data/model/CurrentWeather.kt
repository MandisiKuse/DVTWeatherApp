package com.dvt.weatherapp.data.model

data class CurrentWeather(
    val coord: Coord?,
    val weather: Weather?,
    val base: String?,
    val main: Main?,
    val visibility: Int?,
    val wind: Wind?,
    val rain: Rain?,
    val clouds: Clouds?,
    val dt: Int?,
    val sys: Sys?,
    val timezone: Int?,
    val id: Int?,
    val name: String?,
    val cod: Int?
)