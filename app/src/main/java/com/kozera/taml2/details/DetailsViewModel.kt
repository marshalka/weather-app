package com.kozera.taml2.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kozera.taml2.UiState
import com.kozera.taml2.repository.WeatherRepository
import com.kozera.taml2.repository.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val mutableWeatherDetails = MutableLiveData<UiState<Weather>>()
    val immutableWeatherDetails: LiveData<UiState<Weather>> = mutableWeatherDetails
    @Suppress("unused")
    constructor() : this(WeatherRepository())
    fun getCityWeather(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = weatherRepository.getCityWeather(cityName)
                val weather = response.body()
                mutableWeatherDetails.postValue(UiState(data = weather, isLoading = false, error = null))

            } catch (e: Exception) {
                mutableWeatherDetails.postValue(UiState(data = null, isLoading = false, error = e.message))
                Log.e("DetailsViewModel", "Error occurred", e)
            }
        }
    }
}