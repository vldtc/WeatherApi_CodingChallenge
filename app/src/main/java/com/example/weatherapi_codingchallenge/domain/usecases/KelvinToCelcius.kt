package com.example.weatherapi_codingchallenge.domain.usecases

fun celciusConverter(kelvin: Double?): Int {
    if (kelvin != null) {
        return (kelvin - 273.15).toInt()
    }
    return 0
}