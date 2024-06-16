package com.dvt.weatherapp.ui.screen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.dvt.weatherapp.R
import com.dvt.weatherapp.ui.navigation.Screens
import com.dvt.weatherapp.ui.navigation.localNavController
import com.dvt.weatherapp.ui.statemodel.CurrentWeatherState
import com.dvt.weatherapp.ui.statemodel.Description
import com.dvt.weatherapp.ui.statemodel.WeeklyForecast
import com.dvt.weatherapp.ui.viewmodel.WeatherViewModel
import com.dvt.weatherapp.ui.views.CurrentTemperatureView
import com.dvt.weatherapp.ui.views.CurrentWeatherView
import com.dvt.weatherapp.ui.views.Dialog
import com.dvt.weatherapp.ui.views.Loader
import com.dvt.weatherapp.util.Resource
import com.dvt.weatherapp.util.setBackgroundColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel = koinViewModel()) {

    val currentWeatherState by weatherViewModel.weatherStateFlow.collectAsState()
    val showPermissionDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                weatherViewModel.getCurrentLocation()
            } else {
                showPermissionDialog.value = true
            }
        })

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            weatherViewModel.getCurrentLocation()
        } else {
            showPermissionDialog.value = true
        }
    }

    if (showPermissionDialog.value) {
        PermissionDialog(onConfirm = {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            weatherViewModel.getCurrentLocation()
        }, onDismiss = { (context as Activity).finish() })
    }

    currentWeatherState?.let { weatherNetworkState ->
        when (weatherNetworkState) {
            is Resource.Success -> {
                weatherNetworkState.data?.let { weatherDetails ->
                    WeatherContent(weatherDetails)
                } ?: NetworkErrorDialog(
                    title = stringResource(id = R.string.network_error_heading_weather),
                    onConfirm = { weatherViewModel.getCurrentLocation() },
                    onDismiss = { (context as Activity).finish() }
                )
            }

            is Resource.Error -> {
                NetworkErrorDialog(
                    title = stringResource(id = R.string.network_error_heading_weather),
                    onConfirm = { weatherViewModel.getCurrentLocation() },
                    onDismiss = { (context as Activity).finish() }
                )
            }

            is Resource.Loading-> {
                Loader()
            }
        }
    } ?: Loader()
}

@Composable
private fun WeatherContent(weatherDetails: CurrentWeatherState) {
    val navController = localNavController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = setBackgroundColor(weatherDetails.description))
    ) {
        CurrentWeatherView(
            currentTemperature = weatherDetails.current, weatherType = weatherDetails.description
        )
        CurrentTemperatureView(
            weatherDetails.min,
            weatherDetails.current,
            weatherDetails.max
        )
        ForecastView(weatherDetails.forecast)
        Button(
            onClick = { navController.navigate(Screens.SEARCH_SCREEN.name) },
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .height(40.dp)
        ) {
            Text(text = stringResource(id = R.string.search_title))
        }
    }
}

@Composable
private fun ForecastView(
    weeklyForecast: List<WeeklyForecast>
) {
    LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
        items(weeklyForecast) { forecastWeather ->
            DailyForecastItem(
                day = forecastWeather.day,
                weatherIconRes = setForecastIcon(forecastWeather.description),
                temperature = "${forecastWeather.temp}°"
            )
        }
    }
}

@Composable
private fun DailyForecastItem(
    day: String, weatherIconRes: Int,
    temperature: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = day,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.weight(0.25f)
        )
        Image(
            painter = painterResource(id = weatherIconRes),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .weight(0.5f)
        )
        Text(
            text = temperature,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.weight(0.15f)
        )
    }
}

@Composable
private fun PermissionDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        title = stringResource(R.string.permission_heading),
        message = stringResource(R.string.permission_content),
        positiveButtonTitle = stringResource(R.string.button_ok),
        negativeButtonTitle = stringResource(R.string.button_cancel),
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}

@Composable
private fun NetworkErrorDialog(
    title: String, onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        title = title,
        message = stringResource(R.string.error_content),
        positiveButtonTitle = stringResource(R.string.button_retry),
        negativeButtonTitle = stringResource(R.string.button_cancel),
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}

private fun setForecastIcon(description: Description) = when (description) {
    Description.CLEAR -> R.drawable.ic_clear_large
    Description.CLOUDS -> R.drawable.ic_partlysunny_large
    Description.RAIN, Description.SNOW -> R.drawable.ic_rain_large
}