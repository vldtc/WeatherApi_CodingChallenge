package com.example.weatherapi_codingchallenge.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.example.weatherapi_codingchallenge.data.model.geocoding.GeocodingItemModel
import org.junit.Rule
import org.junit.Test


class CurrentLocationKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchBar_isDisplayed() {
        composeTestRule.setContent {
            SearchBar(
                query = "",
                onSearch = {},
                onLocationClick = {}
            )
        }

        composeTestRule.onNodeWithText("Search location...").assertIsDisplayed()
    }

    @Test
    fun searchBar_withText_displaySearchLocationItem() {
        val location = GeocodingItemModel(
            name = "London",
            state = "England",
            country = "United Kingdom",
            lat = 51.5074,
            lon = -0.1278
        )
        val searchText = "London"

        composeTestRule.setContent {
            Column {
                SearchBar(
                    query = searchText,
                    onSearch = {},
                    onLocationClick = {}
                )
                SearchedLocationItem(
                    location = location,
                    onLocationClick = { _, _ -> }
                )
            }
        }

        composeTestRule.onAllNodesWithText(location.name.toString()).assertCountEquals(2)
        composeTestRule.onAllNodesWithText(location.state.toString()).assertCountEquals(1)
        composeTestRule.onAllNodesWithText(location.country.toString()).assertCountEquals(1)

    }

}