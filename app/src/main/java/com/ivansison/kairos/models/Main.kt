package com.ivansison.kairos.models

import com.google.gson.annotations.SerializedName

class Main (
    @SerializedName("temp")
    var temp: Float = 0.toFloat(),

    @SerializedName("temp_min")
    var temMin: Float = 0.toFloat(),

    @SerializedName("temp_max")
    var temMax: Float = 0.toFloat(),

    @SerializedName("feels_like")
    var feelsLike: Float = 0.toFloat(),

    @SerializedName("humidity")
    var humidity: Float = 0.toFloat(),

    @SerializedName("pressure")
    var pressure: Float = 0.toFloat(),

    @SerializedName("sea_level")
    var levelSea: Float = 0.toFloat(),

    @SerializedName("grnd_level")
    var levelGround: Float = 0.toFloat()
)