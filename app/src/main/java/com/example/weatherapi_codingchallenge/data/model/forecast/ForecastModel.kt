package com.example.weatherapi_codingchallenge.data.model.forecast


import com.google.gson.annotations.SerializedName

data class ForecastModel(
    @SerializedName("city")
    val city: CityModel? = CityModel(),
    @SerializedName("cnt")
    val cnt: Int? = 0,
    @SerializedName("cod")
    val cod: String? = "",
    @SerializedName("list")
    val list: List<ForecastListModel?> = listOf(),
    @SerializedName("message")
    val message: Int? = 0
)