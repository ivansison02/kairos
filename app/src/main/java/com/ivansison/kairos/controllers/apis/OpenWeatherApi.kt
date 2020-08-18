package com.ivansison.kairos.controllers.apis

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class   OpenWeatherApi {
    companion object {
        val ID: String = "bb4ba03149bbdf75c75449ab2299780d"
        var URL = "https://api.openweathermap.org/"
        val URL_ICON = "https://openweathermap.org/img/wn/"
        val ICON_SIZE = "@2x.png"

        private fun getBuilder(): Retrofit {
            return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL)
                .build()
        }

        private fun getService() : OpenWeatherInterface = getBuilder().create(
            OpenWeatherInterface::class.java)

        fun getCurrentWeather(lat: String, lon: String, unit: String) = getService().getCurrentWeather(lat, lon, unit, ID)

        fun getCurrentWeather(q: String, unit: String) = getService().getCurrentWeather(q, unit, ID)
    }
}
