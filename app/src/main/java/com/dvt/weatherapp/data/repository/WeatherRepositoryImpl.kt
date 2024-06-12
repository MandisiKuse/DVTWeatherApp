package com.dvt.weatherapp.data.repository

import com.dvt.weatherapp.data.model.CurrentWeather
import com.dvt.weatherapp.data.model.Forecast
import com.dvt.weatherapp.data.remote.WeatherApi
import com.dvt.weatherapp.domain.repository.WeatherRepository
import com.dvt.weatherapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(private val weatherApi: WeatherApi): WeatherRepository {
    override suspend fun getWeather(lat: Double, lon: Double): Flow<Resource<CurrentWeather>> =
        flow<Resource<CurrentWeather>> {
            emit(Resource.Loading())
            with(weatherApi.getCurrentWeather(lat, lon)) {
                if (!isSuccessful) {
                    emit(Resource.Error(message()))
                } else {
                    body()?.let {
                        emit(Resource.Success(it))
                    } ?: emit(Resource.Error(message()))
                }
            }
        }.catch { emit(Resource.Error(it.localizedMessage)) }

    override suspend fun getForecast(lat: Double, lon: Double): Flow<Resource<Forecast>> =
        flow<Resource<Forecast>> {
            emit(Resource.Loading())
            with(weatherApi.getForecastWeather(lat, lon)) {
                if (!isSuccessful) {
                    emit(Resource.Error(message()))
                } else {
                    body()?.let {
                        emit(Resource.Success(it))
                    } ?: emit(Resource.Error(message()))
                }
            }
        }.catch { emit(Resource.Error(it.localizedMessage)) }
}