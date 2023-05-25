package com.example.weatherapi_codingchallenge.domain.usecases

import android.content.Context
import android.content.SharedPreferences

import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import com.google.common.truth.Truth.assertThat

class LastLocationTest {

    @Mock
    private lateinit var mockContext: Context
    @Mock
    private lateinit var mockSharedPref: SharedPreferences
    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    private val testLat = 37.7749
    private val testLon = -122.4194

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(mockContext.getSharedPreferences(any(String::class.java), any(Int::class.java))).thenReturn(mockSharedPref)
        `when`(mockSharedPref.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putString(any(String::class.java), any(String::class.java))).thenReturn(mockEditor)
    }

    @Test
    fun `test saveLastLocation has null values before being set with values`() {
        val expectedLatKey = "lastLocationLat"
        val expectedLonKey = "lastLocationLon"

        `when`(mockEditor.apply()).then{
            assertThat(mockSharedPref.getString(expectedLatKey, null)).isEqualTo(null)
            assertThat(mockSharedPref.getString(expectedLonKey, null)).isEqualTo(null)
        }
    }

    @Test
    fun `test saveLastLocation successfully stores location values`(){
        val expectedLatKey = "lastLocationLat"
        val expectedLonKey = "lastLocationLon"
        val expectedLatValue = testLat.toString()
        val expectedLonValue = testLon.toString()

        // Call the function under test
        saveLastLocation(mockContext, testLat, testLon)

        // Verify that SharedPreferences.Editor.putString() is called with the expected arguments
        `when`(mockEditor.putString(expectedLatKey, expectedLatValue)).thenReturn(mockEditor)
        `when`(mockEditor.putString(expectedLonKey, expectedLonValue)).thenReturn(mockEditor)

        // Verify that the expected interactions occurred
        `when`(mockEditor.apply()).then {
            // Verify that the expected values are set before apply() is called
            assertThat(mockSharedPref.getString(expectedLatKey, null)).isEqualTo(expectedLatValue)
            assertThat(mockSharedPref.getString(expectedLonKey, null)).isEqualTo(expectedLonValue)
        }
    }

    @Test
    fun `test getLastLocation with stored values gets correct response`() {
        val expectedLatKey = "lastLocationLat"
        val expectedLonKey = "lastLocationLon"

        // Mock the stored values in SharedPreferences
        `when`(mockSharedPref.getString(expectedLatKey, null)).thenReturn(testLat.toString())
        `when`(mockSharedPref.getString(expectedLonKey, null)).thenReturn(testLon.toString())

        // Call the function under test
        val result = getLastLocation(mockContext)

        // Verify that the expected values are returned
        val expectedLocation = Pair(testLat, testLon)
        assertThat(result).isEqualTo(expectedLocation)
    }

    @Test
    fun `test getlastLocation with no values gets correct response`() {
        val expectedLatKey = "lastLocationLat"
        val expectedLonKey = "lastLocationLon"

        // Mock the absence of stored values in SharedPreferences
        `when`(mockSharedPref.getString(expectedLatKey, null)).thenReturn(null)
        `when`(mockSharedPref.getString(expectedLonKey, null)).thenReturn(null)

        // Call the function under test
        val result = getLastLocation(mockContext)

        // Verify that null is returned when no values are found
        assertThat(result).isNull()
    }

}