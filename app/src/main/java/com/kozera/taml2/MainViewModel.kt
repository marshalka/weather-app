package com.kozera.taml2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kozera.taml2.repository.WeatherRepository
import com.kozera.taml2.repository.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository()

    private val mutableWeatherData = MutableLiveData<UiState<List<Weather>>>()
    val immutableWeatherData: LiveData<UiState<List<Weather>>> = mutableWeatherData

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = weatherRepository.getWeatherResponse()
                val weathers = response.body()?.list
                mutableWeatherData.postValue(UiState(data = weathers, isLoading = false, error = null))

            } catch (e: Exception) {
                mutableWeatherData.postValue(UiState(data = null, isLoading = false, error = e.message))
                Log.e("MainViewModel", "Error occurred", e)
            }
        }
    }
}