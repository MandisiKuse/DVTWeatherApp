package com.dvt.weatherapp.util

import com.dvt.weatherapp.ui.statemodel.Description
import com.dvt.weatherapp.ui.theme.CloudyColor
import com.dvt.weatherapp.ui.theme.RainyColor
import com.dvt.weatherapp.ui.theme.SunnyColor

fun setBackgroundColor(description: Description) = when (description) {
    Description.CLEAR -> SunnyColor
    Description.CLOUDS -> CloudyColor
    Description.RAIN, Description.SNOW -> RainyColor
}