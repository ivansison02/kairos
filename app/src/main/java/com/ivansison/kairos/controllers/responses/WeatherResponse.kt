package com.ivansison.kairos.controllers.responses

import com.google.gson.annotations.SerializedName
import com.ivansison.kairos.models.*

class WeatherResponse(
    @SerializedName("coord")
    var coord: Coordinates? = null,

    @SerializedName("weather")
    var weatherType: ArrayList<WeatherType> = ArrayList<WeatherType>(),

    @SerializedName("main")
    var details: Main? = null,

    @SerializedName("wind")
    var wind: Wind? = null,

    @SerializedName("clouds")
    var clouds: Cloud? = null,

    @SerializedName("snow")
    var snow: Snow? = null,

    @SerializedName("rain")
    var rain: Rain? = null,

    @SerializedName("dt")
    var dt: Float = 0.toFloat(),

    @SerializedName("sys")
    var sys: System? = null,

    @SerializedName("timezone")
    var timezone: Float = 0.toFloat(),

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("cod")
    var cod: Float = 0.toFloat()) {
}