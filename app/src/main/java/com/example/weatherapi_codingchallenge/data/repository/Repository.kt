package com.example.weatherapi_codingchallenge.data.repository

import com.example.weatherapi_codingchallenge.data.model.geocoding.GeocodingItemModel
import com.example.weatherapi_codingchallenge.data.model.weather.WeatherModel

interface Repository{

    suspend fun getWeather(latitude: Double, longitude: Double, apiKey: String): WeatherModel

    suspend fun getCoordinates(query: String, limit: Int, apiKey: String): List<GeocodingItemModel>
}