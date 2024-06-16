package com.dvt.weatherapp.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import com.dvt.weatherapp.ui.statemodel.Description
import com.dvt.weatherapp.util.setImage

@Composable
fun CurrentWeatherView(
    currentTemperature: Int,
    weatherType: Description,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.42f)
    ) {
        Image(
            painter = painterResource(id = setImage(weatherType)),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${currentTemperature}°",
                color = Color.White,
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = weatherType.name.toUpperCase(Locale.current),
                color = Color.White,
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}