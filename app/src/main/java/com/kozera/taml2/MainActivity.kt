package com.kozera.taml2

import com.kozera.taml2.details.DetailsActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.kozera.taml2.ui.theme.TAML2Theme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.kozera.taml2.repository.model.Weather


class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getData()

        setContent {
            TAML2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Showcase(viewModel = viewModel, { id: String ->
                        navigateToDetailsActivity(id)
                    })
                }
            }
        }
    }

    fun navigateToDetailsActivity(id: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("CUSTOM_KEY", id)
        startActivity(intent)
    }
}

@Composable
fun Showcase(viewModel: MainViewModel, onItemClick: (String) -> Unit, modifier: Modifier = Modifier) {
    val uiState by viewModel.immutableWeatherData.observeAsState(UiState(isLoading = true))

    when {
        uiState.isLoading -> {
            ShowLoadingIndicator()
        }

        uiState.error != null -> {
            ShowErrorMessage(uiState.error.toString())
        }

        uiState.data != null -> {
            uiState.data?.let { ShowWeatherData(uiState.data, onItemClick, modifier) }
        }
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
fun ShowWeatherData(weathers: List<Weather>?, onItemClick: (String) -> Unit, modifier: Modifier) {
    if (weathers?.isNotEmpty() == true) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(weathers) { weather ->
                WeatherCard(weather = weather, onItemClick = onItemClick)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    } else {
        Text(
            text = "No weather data available.",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WeatherCard(weather: Weather, onItemClick: (String) -> Unit) {
    Log.d("WeatherCard", "City: ${weather.name}, Temperature: ${weather.main.temp}°C")

    val specificImageResource = getSpecificImageResource(weather)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        WeatherCardBackground(imageResource = specificImageResource, modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
            .aspectRatio(1.5f) // adjust this ratio based on your design requirements
        )
        WeatherCardContent(weather = weather, onItemClick = onItemClick)
    }
}
@Composable
fun WeatherCardBackground(imageResource: Int, modifier: Modifier) {
    Image(
        painter = painterResource(id = imageResource),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun WeatherCardContent(weather: Weather, onItemClick: (String) -> Unit) {
    Column(
        modifier = Modifier.clickable { onItemClick.invoke(weather.name) }
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeatherText(weather.name, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        WeatherText("Temperature: ${weather.main.temp}°C", fontSize = 18.sp)
        WeatherText("Feels Like: ${weather.main.feelsLike}°C", fontSize = 18.sp)
        WeatherText("Humidity: ${weather.main.humidity}%", fontSize = 18.sp)
        WeatherText("Pressure: ${weather.main.pressure} hPa", fontSize = 18.sp)
    }
}

@Composable
fun WeatherText(text: String, fontWeight: FontWeight = FontWeight.Normal, fontSize: TextUnit = 16.sp) {
    Text(
        text = text,
        fontWeight = fontWeight,
        fontSize = fontSize,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}

fun getSpecificImageResource(weather: Weather): Int {
    return when {
        weather.main.temp > 18 -> R.drawable.sunny_image
        weather.main.temp > 5 && weather.main.temp < 18 -> R.drawable.cloudy_image
        weather.main.temp < 5 && weather.main.temp > 0 -> R.drawable.rainy_image
        weather.main.temp < 0 -> R.drawable.snowy_image
        else -> R.drawable.default_image
    }
}