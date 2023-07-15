package com.example.weatherapi_codingchallenge.ui.screens

import com.example.weatherapi_codingchallenge.data.model.forecast.ForecastModel
import com.example.weatherapi_codingchallenge.data.model.geocoding.GeocodingItemModel
import com.example.weatherapi_codingchallenge.data.model.geocoding.LocalNamesModel
import com.example.weatherapi_codingchallenge.data.model.weather.CloudsModel
import com.example.weatherapi_codingchallenge.data.model.weather.CoordModel
import com.example.weatherapi_codingchallenge.data.model.weather.MainModel
import com.example.weatherapi_codingchallenge.data.model.weather.SysModel
import com.example.weatherapi_codingchallenge.data.model.weather.WeatherModel
import com.example.weatherapi_codingchallenge.data.model.weather.WindModel
import com.example.weatherapi_codingchallenge.data.repository.Repository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class CurrentLocationViewModelTest {

    @Mock
    private lateinit var repository: Repository

    private lateinit var viewModel: CurrentLocationViewModel

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        viewModel = CurrentLocationViewModel(repository)
    }

    @Test
    fun getWeather_shouldUpdateWeatherState() = runBlocking {
        // Arrange
        val latitude = 37.7749
        val longitude = -122.4194
        val units = "metric"
        val apiKey = "API_KEY"
        val expectedWeather = WeatherModel(base = "", clouds = CloudsModel(), cod = 0, coord = CoordModel(), dt = 0, id = 0, main = MainModel(), name = "", sys = SysModel(), timezone = 0, visibility = 0, weather = listOf(), wind = WindModel())
        val expectedForecast = ForecastModel(city = CityModel(), cnt = 0, cod = "", list = listOf(), message = 0)

        `when`(repository.getWeather(latitude, longitude, units, apiKey)).thenReturn(expectedWeather)
        `when`(repository.getForecast(latitude, longitude, units, apiKey)).thenReturn(expectedForecast)

        // Act
        viewModel.getWeather(latitude, longitude, units, apiKey)

        // Assert
        val weather = viewModel.weather.first()
        val forecast = viewModel.forecast.first()
        assertThat(weather).isEqualTo(expectedWeather)
        assertThat(forecast).isEqualTo(expectedForecast)
    }

    @Test
    fun getLocation_shouldUpdateSearchState() = runBlocking {
        // Arrange
        val query = "San Francisco"
        val apiKey = "API_KEY"
        val expectedResponse = listOf(
            GeocodingItemModel(country = "", lat = 0.0, localNames = LocalNamesModel(), lon = 0.0, name = "", state = ""),
            GeocodingItemModel(country = "", lat = 0.0, localNames = LocalNamesModel(), lon = 0.0, name = "", state = ""),
            GeocodingItemModel(country = "", lat = 0.0, localNames = LocalNamesModel(), lon = 0.0, name = "", state = "")
        )

        `when`(repository.getCoordinates(query, 5, apiKey)).thenReturn(expectedResponse)

        // Act
        viewModel.getLocation(query, apiKey)

        // Assert
        val searchResults = viewModel.search.take(1).toList()[0]
        assertThat(searchResults).isEqualTo(expectedResponse)
    }
}