package com.example.weatherapi_codingchallenge.domain.usecases

import android.content.Context
import android.content.SharedPreferences

// Function to save the last location used in SharedPreferences
fun saveLastLocation(context: Context, lat: Double?, lon: Double?) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPrefs.edit()
    editor.putString("lastLocationLat", lat.toString())
    editor.putString("lastLocationLon", lon.toString())
    editor.apply()
}

// Function to retrieve the last location used from SharedPreferences
fun getLastLocation(context: Context): Pair<Double, Double>? {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)
    val lat = sharedPrefs.getString("lastLocationLat", null)?.toDoubleOrNull()
    val lon = sharedPrefs.getString("lastLocationLon", null)?.toDoubleOrNull()
    return if (lat != null && lon != null) {
        Pair(lat, lon)
    } else {
        null
    }
}