package com.kozera.taml2.repository.model

data class Weather(
    val id: Number,
    val name: String,
    val main: WeatherInfo,
)

data class WeatherInfo(
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val seaLevel: Int,
    val groundLevel: Int
)