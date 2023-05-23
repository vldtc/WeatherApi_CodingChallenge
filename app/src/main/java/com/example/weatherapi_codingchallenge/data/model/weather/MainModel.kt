package com.example.weatherapi_codingchallenge.data.model.weather


import com.google.gson.annotations.SerializedName

data class MainModel(
    @SerializedName("feels_like")
    val feelsLike: Double? = 0.0,
    @SerializedName("humidity")
    val humidity: Int? = 0,
    @SerializedName("pressure")
    val pressure: Int? = 0,
    @SerializedName("temp")
    val temp: Double? = 0.0,
    @SerializedName("temp_max")
    val tempMax: Double? = 0.0,
    @SerializedName("temp_min")
    val tempMin: Double? = 0.0
)