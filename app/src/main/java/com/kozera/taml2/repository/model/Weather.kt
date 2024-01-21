package com.kozera.taml2.repository.model

import com.google.gson.annotations.SerializedName

data class Weather(
    val id: Number,
    val name: String,
    val main: WeatherInfo,
)

data class WeatherInfo(
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
)