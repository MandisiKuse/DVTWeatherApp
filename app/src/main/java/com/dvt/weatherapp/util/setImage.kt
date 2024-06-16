package com.dvt.weatherapp.util

import com.dvt.weatherapp.R
import com.dvt.weatherapp.ui.statemodel.Description

fun setImage(description: Description) = when (description) {
    Description.CLEAR -> R.drawable.bg_forest_sunny
    Description.CLOUDS -> R.drawable.bg_forest_cloudy
    Description.RAIN, Description.SNOW -> R.drawable.bg_forest_rainy
}