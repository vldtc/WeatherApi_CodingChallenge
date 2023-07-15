package com.example.weatherapi_codingchallenge.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapi_codingchallenge.data.model.forecast.ForecastModel
import com.example.weatherapi_codingchallenge.data.model.geocoding.GeocodingItemModel
import com.example.weatherapi_codingchallenge.data.model.weather.WeatherModel
import com.example.weatherapi_codingchallenge.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrentLocationViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _weather = MutableStateFlow(WeatherModel())
    val weather: StateFlow<WeatherModel> = _weather


    private val _search = MutableStateFlow<List<GeocodingItemModel>>(emptyList())
    val search: StateFlow<List<GeocodingItemModel>> = _search

    private val _forecast = MutableStateFlow(ForecastModel())
    val forecast : StateFlow<ForecastModel> = _forecast

    fun getWeather(latitude: Double?, longitude: Double?, units: String, apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(NonCancellable) {
                val responseWeather = repository.getWeather(latitude, longitude, units, apiKey)
                val responseForecast = repository.getForecast(latitude, longitude, units, apiKey)
                _weather.value = responseWeather
                _forecast.value = responseForecast
            }
        }
    }

    fun getLocation(query: String, apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCoordinates(query, 5, apiKey)
            _search.value = response

        }
    }
}