package com.dvt.weatherapp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dvt.weatherapp.R

@Composable
fun CurrentTemperatureView(
    minimum: Int,
    current: Int,
    maximum: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TemperatureItem(label = stringResource(R.string.min), value = "$minimum°")
        TemperatureItem(label = stringResource(R.string.current), value = "$current°")
        TemperatureItem(label = stringResource(R.string.max), value = "$maximum°")
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .height(1.dp)
            .background(color = Color.White)
    )
}