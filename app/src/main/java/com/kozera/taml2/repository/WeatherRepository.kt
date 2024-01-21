package com.kozera.taml2.repository

import com.kozera.taml2.repository.model.Weather
import com.kozera.taml2.repository.model.WeatherResponse
import retrofit2.Response

class WeatherRepository {
    suspend fun getWeatherResponse(): Response<WeatherResponse> =
        WeatherService.weatherService.getWeatherResponse()

    suspend fun getCityWeather(cityName: String): Response<Weather> =
        WeatherService.weatherService.getCityWeather(cityName, "metric", "65be2f217af0973a83fe8d9fac0d24c8")
}