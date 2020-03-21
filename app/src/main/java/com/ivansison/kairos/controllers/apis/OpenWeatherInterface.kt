package com.ivansison.kairos.controllers.apis

import com.ivansison.kairos.controllers.responses.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherInterface {
    @GET("data/2.5/weather?")
    fun getCurrentWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("APPID") app_id: String): Call<WeatherResponse>

    @GET("data/2.5/weather?")
    fun getCurrentWeather(@Query("q") q: String, @Query("APPID") app_id: String): Call<WeatherResponse>
}