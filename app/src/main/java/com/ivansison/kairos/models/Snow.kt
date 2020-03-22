package com.ivansison.kairos.models

import com.google.gson.annotations.SerializedName

class Snow(
    @SerializedName("3h")
    var h3: Float = 0.toFloat(),

    @SerializedName("1h")
    var h1: Float = 0.toFloat()
)