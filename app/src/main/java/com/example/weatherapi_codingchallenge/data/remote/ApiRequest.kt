package com.example.weatherapi_codingchallenge.data.remote

import com.example.weatherapi_codingchallenge.data.model.geocoding.GeocodingItemModel
import com.example.weatherapi_codingchallenge.data.model.weather.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequest {

    @GET(ApiDetails.WEATHER)
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): WeatherModel

    @GET(ApiDetails.GEOCODING)
    suspend fun getCoordinates(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String
    ): List<GeocodingItemModel>

}