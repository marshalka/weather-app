package com.kozera.taml2.repository

import com.kozera.taml2.repository.model.Weather
import com.kozera.taml2.repository.model.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/data/2.5/group?id=2643743,2988507,5128581,847633,4984692,2542997,2643123,5815135&units=metric&appid=65be2f217af0973a83fe8d9fac0d24c8")
    suspend fun getWeatherResponse(): Response<WeatherResponse>

    @GET("/data/2.5/weather")
    suspend fun getCityWeather(@Query("q") cityName: String, @Query("units") units: String, @Query("appid") apiKey: String): Response<Weather>

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org"
        private val retrofit: Retrofit by lazy {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        val weatherService: WeatherService by lazy {
            retrofit.create(WeatherService::class.java)
        }
    }
}
