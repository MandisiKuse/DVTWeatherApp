package com.dvt.weatherapp.di

import com.dvt.weatherapp.data.remote.WeatherApi
import com.dvt.weatherapp.data.repository.CurrentLocationRepositoryImpl
import com.dvt.weatherapp.data.repository.WeatherRepositoryImpl
import com.dvt.weatherapp.domain.mapper.CityWeatherMapper
import com.dvt.weatherapp.domain.mapper.WeatherMapper
import com.dvt.weatherapp.domain.repository.CurrentLocationRepository
import com.dvt.weatherapp.domain.repository.WeatherRepository
import com.dvt.weatherapp.ui.viewmodel.WeatherViewModel
import com.dvt.weatherapp.util.AppConstants
import com.google.android.gms.location.LocationServices
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val serviceModule = module {
    single { provideRetrofit() }

    factory { get<Retrofit>().create(WeatherApi::class.java) }
}

val mapperModule = module {
    single { WeatherMapper() }

    single { CityWeatherMapper() }
}

val repositoryModule = module {
    single {
        CurrentLocationRepositoryImpl(
            LocationServices.getFusedLocationProviderClient(androidContext())
        )
    } withOptions { bind<CurrentLocationRepository>() }

    single { WeatherRepositoryImpl(get()) } withOptions { bind<WeatherRepository>() }
}

val viewModelModule = module {
    single { WeatherViewModel(get(),get(),get(), get()) }
}

val allModules = listOf(serviceModule, mapperModule, repositoryModule, viewModelModule)

private fun provideRetrofit(): Retrofit =
    Retrofit.Builder()
        .baseUrl(AppConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideOkHttpClient())
        .build()

private fun provideOkHttpClient(): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()