package com.ivansison.kairos.models

import com.google.gson.annotations.SerializedName

class Coordinates(
    @SerializedName("lon")
    var latitude: Double,

    @SerializedName("lat")
    var longitude: Double
)