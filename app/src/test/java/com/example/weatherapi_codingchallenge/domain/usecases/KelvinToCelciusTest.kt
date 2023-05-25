package com.example.weatherapi_codingchallenge.domain.usecases

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class KelvinToCelciusTest {

    @Test
    fun `should convert 300 kelvins to 26 celcius`() {
        //When
        val kelvin= 300.0
        val expectedCelcius= 26
        //If
        val result = celciusConverter(kelvin)
        //Then
        assertThat(result).isEqualTo(expectedCelcius)
    }

    @Test
    fun `should convert 200 kelvins to negative 73 celcius`() {
        //When
        val kelvin= 200.0
        val expectedCelcius= -73
        //If
        val result = celciusConverter(kelvin)
        //Then
        assertThat(result).isEqualTo(expectedCelcius)
    }

    @Test
    fun `should convert negative 2 kelvins to negative 275 celcius`() {
        //When
        val kelvin= -2.0
        val expectedCelcius= -275
        //If
        val result = celciusConverter(kelvin)
        //Then
        assertThat(result).isEqualTo(expectedCelcius)
    }

    @Test
    fun `should return 0 celcius when kelvin is null`() {
        //When
        val kelvin: Double?= null
        val expectedCelcius= 0
        //If
        val result = celciusConverter(kelvin)
        //Then
        assertThat(result).isEqualTo(expectedCelcius)
    }


}