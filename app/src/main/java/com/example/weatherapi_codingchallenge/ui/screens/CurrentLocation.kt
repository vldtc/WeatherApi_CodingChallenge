package com.example.weatherapi_codingchallenge.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapi_codingchallenge.BuildConfig
import com.example.weatherapi_codingchallenge.data.model.forecast.ForecastModel
import com.example.weatherapi_codingchallenge.data.model.geocoding.GeocodingItemModel
import com.example.weatherapi_codingchallenge.data.model.weather.WeatherModel
import com.example.weatherapi_codingchallenge.domain.usecases.getLastLocation
import com.example.weatherapi_codingchallenge.domain.usecases.saveLastLocation
import com.example.weatherapi_codingchallenge.ui.theme.md_theme_light_inversePrimary
import com.example.weatherapi_codingchallenge.ui.theme.md_theme_light_primary
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CurrentLocationScreen() {

    val context = LocalContext.current

    val viewModel = hiltViewModel<CurrentLocationViewModel>()
    val weather by viewModel.weather.collectAsState()
    val search by viewModel.search.collectAsState()
    val forecast by viewModel.forecast.collectAsState()
    var query by remember { mutableStateOf("") }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, fetch current location
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val lat = location.latitude
                        val lon = location.longitude

                        viewModel.getWeather(lat, lon, "metric", BuildConfig.API_KEY)
                    }
                }
            Toast.makeText(context, "Your current location has been set!", Toast.LENGTH_SHORT)
                .show()
        } else {
            // Permission denied, handle accordingly
            Toast.makeText(
                context,
                "Location permission has been denied. You have to allow it manually!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val onLocationClick: () -> Unit = {
        if (hasLocationPermission) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val lat = location.latitude
                        val lon = location.longitude

                        viewModel.getWeather(lat, lon, "metric", BuildConfig.API_KEY)
                        Toast.makeText(
                            context,
                            "Your current location has been set!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    val displayLocationWeather: (Double?, Double?) -> Unit = { lat, lon ->
        viewModel.getWeather(lat, lon, "metric", BuildConfig.API_KEY)
        query = ""

        saveLastLocation(context, lat, lon)
    }

    // Retrieve the last location used (if any)
    val lastLocation = getLastLocation(context)
    if (lastLocation != null) {
        LaunchedEffect(lastLocation) {
            // Fetch weather data for the last location used
            viewModel.getWeather(
                lastLocation.first,
                lastLocation.second,
                "metric",
                BuildConfig.API_KEY
            )
        }
    }

    // USER INTERFACE SCREEN LAYOUT
    Column {
        SearchBar(
            query = query, onSearch = { value ->
                query = value
            },
            onLocationClick = onLocationClick
        )
        if (query.isNotBlank()) {
            viewModel.getLocation(query, BuildConfig.API_KEY)
            LazyColumn {
                items(search) { item ->
                    SearchedLocationItem(location = item, displayLocationWeather)
                }
            }
        }
        if (lastLocation != null) {
            LocationWeather(location = weather, forecast = forecast)
        } else {
            Text(
                text = "Select a location to view the weather!",
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onSearch: (String) -> Unit,
    onLocationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = query,
            onValueChange = { onSearch(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = md_theme_light_inversePrimary
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = onLocationClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Current Location",
                        tint = md_theme_light_inversePrimary,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            },
            modifier = Modifier
                .weight(1f),
            singleLine = true,
            placeholder = { Text(text = "Search location...") },
            textStyle = TextStyle(color = Color.Black)
        )


    }
}

@Composable
fun SearchedLocationItem(
    location: GeocodingItemModel,
    onLocationClick: (Double?, Double?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 36.dp, vertical = 8.dp)
            .clickable {
                onLocationClick(location.lat, location.lon)
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
    location: WeatherModel,
    forecast: ForecastModel
) {
    val offsetY = remember { Animatable(1000f) }

    LaunchedEffect(Unit) {
        offsetY.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
    }

    Column(
        modifier = Modifier
            .offset(y = offsetY.value.dp)
    ) {
        val temp = location.main?.temp
        WeatherContent(
            temp = temp,
            location = location
        )
        LazyColumn{
            item{
                ForecastNext24hContent(forecast = forecast)
                ForecastNextDaysContent(forecast = forecast)
            }
        }
    }
}

@Composable
private fun WeatherContent(
    location: WeatherModel,
    temp: Double?
) {
    val scale = remember { Animatable(1.0f) }
    val rotationIcon = remember { Animatable(0f) }
    val scaleIcon = remember { Animatable(1f) }

    LaunchedEffect(temp) {
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(location.weather) {
        while (true) {
            val randomRotation = (-15..15).random().toFloat()
            rotationIcon.animateTo(
                targetValue = randomRotation,
                animationSpec = tween(durationMillis = 5000, easing = FastOutSlowInEasing)
            )
        }
    }

    LaunchedEffect(location.weather) {
        while (true) {
            scaleIcon.animateTo(
                targetValue = 1.2f,
                animationSpec = tween(durationMillis = 5000, easing = LinearEasing)
            )
            scaleIcon.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(durationMillis = 5000, easing = LinearEasing)
            )
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = "${temp?.toInt()}°",
                    style = TextStyle(fontSize = 80.sp, fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.scale(scale.value)
                )
                Text(text = "${location.weather?.getOrNull(0)?.description?.uppercase()}")
            }
            Image(
                painter = rememberAsyncImagePainter(
                    model = "https://openweathermap.org/img/wn/${location.weather?.getOrNull(0)?.icon ?: ""}@2x.png"
                ),
                contentDescription = "icon",
                modifier = Modifier
                    .size(160.dp)
                    .rotate(rotationIcon.value)
                    .scale(scaleIcon.value)
            )
        }
        Text(
            text = "${location.name}",
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 16.dp)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Feels like\n${location.main?.feelsLike?.toInt()}°",
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Min Temp\n${location.main?.tempMin?.toInt()}°",
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Max Temp\n${location.main?.tempMax?.toInt()}°",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ForecastNext24hContent(
    forecast: ForecastModel
) {
    val first9EntriesList = forecast.list.take(12)
    val hourFormat = SimpleDateFormat("ha", Locale.getDefault())

    LazyRow(
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 8.dp)
    ){
        itemsIndexed(first9EntriesList){index, item ->
            Card(
                modifier = Modifier
                    .padding(end = if (index == first9EntriesList.lastIndex) 0.dp else 12.dp)
            ) {
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(item?.dtTxt.toString())
                val formattedHour = hourFormat.format(date as Date)
                Column(
                    modifier = Modifier
                        .padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = formattedHour)
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = "https://openweathermap.org/img/wn/${item?.weather?.getOrNull(0)?.icon ?: ""}@2x.png"
                        ),
                        contentDescription = "icon",
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Text(text = "${item?.main?.temp?.toInt()}°")
                }
            }
        }
    }
}

@Composable
fun ForecastNextDaysContent(
    forecast: ForecastModel
) {

    val filteredByDay = forecast.list.groupBy { item ->
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val timestamp = item?.dtTxt
        val date = dateFormat.parse(timestamp.toString())

        val calendar = Calendar.getInstance()
        calendar.time = date as Date

        val day = calendar.get(Calendar.DAY_OF_MONTH)

        day
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 16.dp)
    ) {
        Card{
            filteredByDay.forEach { (day, forecastItems) ->
                val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_MONTH, day - calendar.get(Calendar.DAY_OF_MONTH))
                val dayOfWeek = dateFormat.format(calendar.time)

                val maxTempMax =
                    forecastItems.maxByOrNull { it?.main?.temp!! }?.main?.temp ?: 0.0
                val minTempMin =
                    forecastItems.minByOrNull { it?.main?.temp!! }?.main?.temp ?: 0.0

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = dayOfWeek, modifier = Modifier.weight(2f))
                    Text(text = "${maxTempMax.toInt()}°", modifier = Modifier.weight(0.5f), textAlign = TextAlign.End)
                    Text(text = "${minTempMin.toInt()}°", modifier = Modifier.weight(0.5f), textAlign = TextAlign.End)
                }
            }
        }
    }
}

