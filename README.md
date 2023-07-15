# Weather App - WeatherApi - Coding Challenge

This project has been developed to resolve Coding Challenge . It is built to be an Weather App that saves your last location and let's you quickly check the weather at your current location.

**Weather App designed in Kotlin as an Android Application**.

# Table of contents
1. [General information](#general-information)
   1. [Input](#input)
   2. [Output](#output)
2. [Screenshots](#screenshots)
3. [Technologies](#technologies)
4. [Testing](#testing)
5. [Future improvements](#future-improvements)


## General information

WeatherApp is an essential weather app that let's you qucikly check the weather around the globe. Just search for a location, or use the dedicated button to check the weather at your exact location.

### Input

You must provide:
* A location by typing it in the search bar.
* You are prompted with a list of possible locations that match your search.
* Or, use the dedicated button to check the weather at your location



An example would be:
```
Search "London"
You are prompted with a list :
  London | England | GB
  City of London | England | GB
  London | Ontario | CA
  Chelsea | England | GB
  London | Kentucky | US

Then, you select the first option. London | England | GB

The weather will be updated according to London | England | GB

```

### Output

After you select a location to check the weather you will see:

  Temperature ( celcius )
  Weather conditon ( using an image indicator )
  Weather conditon ( using text e.g. Scattered Clouds )
  Location name
  Feels like
  Min Temperature
  Max Temperature
  Next 33hours forecast
  Next 5 days forecast
  
## Screenshots

![Screen_Recording_20230715_135446_WeatherApi_CodingChallenge](https://github.com/vldtc/WeatherApi_CodingChallenge/assets/129045490/c52007a9-8f2f-438e-bbb9-86c3947a305b) ![Screenshot 2023-07-15 at 14 10 48](https://github.com/vldtc/WeatherApi_CodingChallenge/assets/129045490/e65e505c-2c20-4b55-a1b8-bd06dc852cc3)

## Technologies

- **Kotlin** is used as primary programming language.
- **Jetpack Compose** is the toolkit used for building the UI.
- **MVVM** architecture.
- **Hilt**, **Retrofit**, **OKHttp** for Api Calls and DI.
- **Coil** library for image loading



## Testing

**JUnit4**, **Mockito** and **Truth** libraries were used for testing.

Unit testing for 
  -> RepoImpl
  -> LastLocation
  -> CurrentLocationViewModel

UI testing for 
  -> CurrentLocation

## Future improvements

   - Room local caching
   - Accessibility features
   - More API features such as Basic Weather API
