# CurrentWeather
A simple android weather app.

The app support fetching todays weather data and the data for the upcoming week.
It can searches for weather data using the users current location or it can 
search for a location by name.
The last location that the user searches for is saved for the next time the user uses the app.

The app also has some simple error handling, like showing a not connected dialog in case the 
the user tries to use the app while there is no internet connection.
Some other cases have not been handled yet, but from time to time i will comeback and fix them.

## How to run the project

You need to have a weather api key from [openweathermap.org](https://openweathermap.org).

Once you have the key import the project to Android Studio and create a new constant 
called WEATHER_API_KEY either in the WeatherApiService file or on a separate file.
