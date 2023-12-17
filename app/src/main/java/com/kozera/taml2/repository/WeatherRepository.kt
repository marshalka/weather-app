package com.kozera.taml2.repository

import com.kozera.taml2.repository.model.WeatherResponse
import retrofit2.Response

class WeatherRepository {
    suspend fun getWeatherResponse(): Response<WeatherResponse> =
        WeatherService.weatherService.getWeatherResponse()

}