package com.ivansison.kairos.utils

import android.content.Context
import com.ivansison.kairos.R
import com.ivansison.kairos.models.*
import java.util.ArrayList

object WeatherDetailUtil {

    fun getDetails(context: Context, details: Main?, cloud: Cloud?, wind: Wind?, rain: Rain?, snow: Snow?): ArrayList<WeatherDetail> {
        var weatherDetails = ArrayList<WeatherDetail>()

        // Weather Main Details
        weatherDetails.add(WeatherDetail(context.getString(R.string.view_settings_temperature), details?.temp.toString(), "", ""))
        weatherDetails.add(WeatherDetail(context.getString(R.string.view_settings_humidity), details?.humidity.toString(), "", ""))
        weatherDetails.add(WeatherDetail(context.getString(R.string.view_settings_temperature_max), details?.temMax.toString(),  "", ""))
        weatherDetails.add(WeatherDetail(context.getString(R.string.view_settings_temperature_min), details?.temMin.toString(), "", ""))
        weatherDetails.add(WeatherDetail(context.getString(R.string.view_settings_level_sea), details?.levelSea.toString(),  "", ""))
        weatherDetails.add(WeatherDetail(context.getString(R.string.view_settings_level_ground), details?.levelGround.toString(),  "", ""))
        weatherDetails.add(WeatherDetail(context.getString(R.string.view_settings_pressure), details?.pressure.toString(), "", ""))
        weatherDetails.add(WeatherDetail(context.getString(R.string.view_settings_feels_like), details?.feelsLike.toString(), "", ""))

        // Clouds
        if (cloud != null) weatherDetails.add(WeatherDetail(context.getString(R.string.view_settings_cloudiness), cloud?.all.toString(),  "", ""))

        // Wind
        if (wind != null) weatherDetails.add(WeatherDetail(context.getString(R.string.view_settings_wind), wind?.speed.toString(),  "", ""))

        // Rain
        if (rain != null)  weatherDetails.add(WeatherDetail(context.getString(R.string.view_settings_precipitation), rain?.h1.toString(),  "", ""))

        // Snow
        if (snow != null) weatherDetails.add(WeatherDetail(context.getString(R.string.view_settings_snow), snow?.h1.toString(),  "", ""))

        return weatherDetails
    }
}