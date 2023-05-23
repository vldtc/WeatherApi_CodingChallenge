package com.example.weatherapi_codingchallenge.ui.screens

import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.size.Size
import com.example.weatherapi_codingchallenge.BuildConfig
import com.example.weatherapi_codingchallenge.data.model.geocoding.GeocodingItemModel
import com.example.weatherapi_codingchallenge.data.model.weather.WeatherModel
import com.example.weatherapi_codingchallenge.domain.usecases.celciusConverter
import com.example.weatherapi_codingchallenge.domain.usecases.getLastLocation
import com.example.weatherapi_codingchallenge.domain.usecases.saveLastLocation
import com.example.weatherapi_codingchallenge.ui.theme.md_theme_light_primary

@Composable
fun CurrentLocationScreen() {

    val context = LocalContext.current

    val viewModel = hiltViewModel<CurrentLocationViewModel>()
    val weather by viewModel.weather.collectAsState()
    val search by viewModel.search.collectAsState()
    var query by remember { mutableStateOf("") }

    val displayLocationWeather: (Double, Double) -> Unit = { lat, lon ->
        viewModel.getWeather(lat, lon, BuildConfig.API_KEY)
        query = ""

        saveLastLocation(context, lat, lon)
    }

    // Retrieve the last location used (if any)
    val lastLocation = getLastLocation(context)
    if (lastLocation != null) {
        LaunchedEffect(lastLocation) {
            // Fetch weather data for the last location used
            viewModel.getWeather(lastLocation.first, lastLocation.second, BuildConfig.API_KEY)
        }
    }


    Column() {
        SearchBar(query = query) { value ->
            query = value
        }
        if (query.isNotBlank()) {
            viewModel.getLocation(query, BuildConfig.API_KEY)
            LazyColumn {
                items(search) { item ->
                    SearchedLocationItem(location = item, displayLocationWeather)
                }
            }
        }
        LocationWeather(location = weather)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onSearch: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = {
            onSearch(it)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp),
        singleLine = true,
        placeholder = { Text(text = "Search location...") },
        textStyle = TextStyle(color = Color.Black)
    )
}

@Composable
fun SearchedLocationItem(
    location: GeocodingItemModel,
    onLocationClick: (Double, Double) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 36.dp, vertical = 8.dp)
            .clickable {
                onLocationClick(location.lat!!, location.lon!!)
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${location.name}",
            modifier = Modifier
                .padding(end = 4.dp)
                .weight(1f)
        )
        Text(
            text = "${location.state}",
            modifier = Modifier
                .padding(end = 4.dp)
                .weight(1f)
        )
        Text(
            text = "${location.country}",
            modifier = Modifier
                .weight(0.5f),
            textAlign = TextAlign.End
            )
    }
    Divider(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        thickness = 1.dp,
        color = md_theme_light_primary
    )
}

@Composable
fun LocationWeather(
    location: WeatherModel
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 0.dp)
    ) {
        val kelvin = location.main?.temp
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = "${celciusConverter(kelvin)}°",
                    style = TextStyle(
                        fontSize = 80.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                Text(text = "${location.weather?.getOrNull(0)?.main}")
            }
            Image(
                painter = rememberAsyncImagePainter(model = "https://openweathermap.org/img/wn/${location.weather?.getOrNull(0)?.icon}@2x.png"),
                contentDescription = "icon",
                modifier = Modifier
                    .size(160.dp)
            )
        }
        Text(text = "${location.name}")
        Text(text = "${location.id}")
        Text(text = "${location.main}")
        Text(text = "${location.timezone}")
        Text(text = "${location.coord?.lat}")
        Text(text = "${location.coord?.lon}")


    }
}

