package com.ivansison.kairos.utils

import com.ivansison.kairos.controllers.responses.WeatherResponse
import retrofit2.Response

object ResponseUtil {

    fun isSuccessful(responseCode: Int) = when (responseCode) {
        200 -> true
        else -> false
    }

    fun getErrorMessage(response: Response<WeatherResponse>, location: String) = when {
        response.toString().toLowerCase().contains("not found") -> "Location: ${location} can't be found."
        else -> "There is a problem with your internet problem or server. Please try again later."
    }

    fun getErrorMessage(response: String) = when {
        response.toLowerCase().contains("exception") -> "There is a problem with your internet problem or server. Please try again later."
        else -> "There is a network problem. Please try again later."
    }
}