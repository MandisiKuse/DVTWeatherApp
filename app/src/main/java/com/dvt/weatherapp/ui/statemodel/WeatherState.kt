package com.dvt.weatherapp.ui.statemodel

data class CurrentWeatherState(
    val current: Int = 0,
    val min: Int = 0,
    val max: Int = 0,
    val description: Description = Description.CLEAR,
    val forecast: List<WeeklyForecast> = emptyList()
)

data class WeatherByCityState(
    val current: Int = 0,
    val min: Int = 0,
    val max: Int = 0,
    val description: Description = Description.CLEAR
)


data class WeeklyForecast(
    val day: String = "",
    val temp: Int = 0,
    val description: Description = Description.CLEAR
)

enum class Description(main: String) {
    CLEAR("Clear"),
    CLOUDS("Clouds"),
    RAIN("Rain"),
    SNOW("Snow")
}