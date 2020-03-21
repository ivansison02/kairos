package com.ivansison.kairos.models

import com.google.gson.annotations.SerializedName

class System {
    @SerializedName("country")
    var country: String? = null

    @SerializedName("sunrise")
    var sunrise: Long = 0

    @SerializedName("sunset")
    var sunset: Long = 0
}