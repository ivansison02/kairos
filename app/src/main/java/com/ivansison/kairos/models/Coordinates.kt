package com.ivansison.kairos.models

import com.google.gson.annotations.SerializedName

class Coordinates(var latitude: Double, var longitude: Double) {
    @SerializedName("lon")
    var lon: Float = 0.toFloat()

    @SerializedName("lat")
    var lat: Float = 0.toFloat()
}