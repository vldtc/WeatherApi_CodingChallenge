package com.example.weatherapi_codingchallenge.data.repository

import com.example.weatherapi_codingchallenge.data.model.geocoding.GeocodingItemModel
import com.example.weatherapi_codingchallenge.data.model.weather.WeatherModel
import com.example.weatherapi_codingchallenge.data.remote.ApiRequest
import javax.inject.Inject

class RepoImpl @Inject constructor(
    val apiRequest: ApiRequest
): Repository{

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ): WeatherModel = apiRequest.getWeather(latitude, longitude, apiKey)

    override suspend fun getCoordinates(
        query: String,
        limit: Int,
        apiKey: String
    ): List<GeocodingItemModel> = apiRequest.getCoordinates(query, limit, apiKey)

}