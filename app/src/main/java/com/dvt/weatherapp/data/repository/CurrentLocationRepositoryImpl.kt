package com.dvt.weatherapp.data.repository

import android.annotation.SuppressLint
import android.location.Location
import com.dvt.weatherapp.domain.repository.CurrentLocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CurrentLocationRepositoryImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : CurrentLocationRepository {
    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location = suspendCoroutine {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { lastLocation ->
            if (lastLocation == null) {
                it.resumeWithException(Exception("Location not found"))
            } else {
                it.resume(lastLocation)
            }
        }
    }
}