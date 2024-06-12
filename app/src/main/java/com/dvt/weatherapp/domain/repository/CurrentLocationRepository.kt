package com.dvt.weatherapp.domain.repository

import android.location.Location

interface CurrentLocationRepository {
    suspend fun getCurrentLocation(): Location
}