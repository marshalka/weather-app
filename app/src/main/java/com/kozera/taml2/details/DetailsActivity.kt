package com.kozera.taml2.details

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kozera.taml2.UiState
import com.kozera.taml2.repository.model.Weather
import com.kozera.taml2.ui.theme.TAML2Theme

class DetailsActivity : ComponentActivity() {

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cityName = intent.getStringExtra("CUSTOM_KEY")

        if (cityName != null) {
            viewModel.getCityWeather(cityName)
        }

        setContent {
            TAML2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        AppBar(
                            title = {
                                Text(text = "Weather Details - $cityName", fontSize = 20.sp)
                            },
                            onBackClick = { finish() }
                        )

                        val uiState by viewModel.immutableWeatherDetails.observeAsState(
                            UiState(
                                isLoading = true
                            )
                        )

                        when (uiState.isLoading) {
                            true -> {
                                ShowLoadingIndicator()
                            }

                            false -> {
                                when {
                                    uiState.error != null -> {
                                        ShowErrorMessage(uiState.error)
                                    }

                                    uiState.data != null -> {
                                        uiState.data?.let { ShowWeatherDetails(it) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ShowWeatherDetails(weather: Weather) {
        Column {
            Text("City: ${weather.name}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Temperature: ${weather.main.temp}°C", fontSize = 18.sp)
            Text("Feels Like: ${weather.main.feelsLike}°C", fontSize = 18.sp)
            Text("Min Temperature: ${weather.main.tempMin}°C", fontSize = 18.sp)
            Text("Max Temperature: ${weather.main.tempMax}°C", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Pressure: ${weather.main.pressure} hPa", fontSize = 18.sp)
            Text("Humidity: ${weather.main.humidity}%", fontSize = 18.sp)
        }
    }

    @Composable
    fun ShowLoadingIndicator() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }

    @Composable
    fun ShowErrorMessage(error: String?) {
        if (error != null) {
            Text(
                text = "Error: $error",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun AppBar(title: @Composable () -> Unit, onBackClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .clickable { onBackClick.invoke() }
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            title()
        }
    }
}
