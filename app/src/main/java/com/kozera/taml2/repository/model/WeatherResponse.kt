package com.kozera.taml2.repository.model

data class WeatherResponse(
    val cnt: Int,
    val list: List<Weather>
)