package com.dvt.weatherapp.ui.screen

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dvt.weatherapp.R
import com.dvt.weatherapp.ui.navigation.localNavController
import com.dvt.weatherapp.ui.statemodel.WeatherByCityState
import com.dvt.weatherapp.ui.viewmodel.WeatherViewModel
import com.dvt.weatherapp.ui.views.CurrentTemperatureView
import com.dvt.weatherapp.ui.views.CurrentWeatherView
import com.dvt.weatherapp.ui.views.Dialog
import com.dvt.weatherapp.util.Resource
import com.dvt.weatherapp.util.setBackgroundColor
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
fun FullScreenSearch(weatherViewModel: WeatherViewModel = koinViewModel()) {

    val context = LocalContext.current
    val weatherByCityState by weatherViewModel.weatherByCityStateFlow.collectAsState()
    val navController = localNavController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        var searchText by remember { mutableStateOf("") }
        var textInput by remember { mutableStateOf("") }

        IconButton(
            onClick = { navController.popBackStack() }
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
        }

        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            label = { Text(stringResource(id = R.string.search_hint)) },
            trailingIcon = {
                IconButton(
                    onClick = {
                        weatherViewModel.getCityWeather(textInput)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        modifier = Modifier
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    searchText = textInput
                }
            )
        )
        Spacer(modifier = Modifier.weight(0.8f))

        weatherByCityState?.let { weatherNetworkState ->
            when (weatherNetworkState) {
                is Resource.Success -> {
                    weatherNetworkState.data?.let { weatherDetails ->
                        WeatherContent(weatherDetails)
                    } ?: NetworkErrorDialog(
                        title = stringResource(id = R.string.network_error_heading_weather),
                        onConfirm = { weatherViewModel.getCityWeather(textInput) },
                        onDismiss = { (context as Activity).finish() }
                    )
                }

                is Resource.Error -> {
                    NetworkErrorDialog(
                        title = stringResource(id = R.string.network_error_heading_weather),
                        onConfirm = { weatherViewModel.getCityWeather(textInput) },
                        onDismiss = { (context as Activity).finish() }
                    )
                }

                is Resource.Loading -> {
                    Spacer(modifier = Modifier.weight(0.8f))

                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = stringResource(id = R.string.search_title),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun WeatherContent(weatherDetails: WeatherByCityState) {

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
    }
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