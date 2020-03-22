package com.ivansison.kairos.utils

import android.content.Context
import com.ivansison.kairos.R
import com.ivansison.kairos.models.Unit as UnitType

object DummyUtil {

    fun getUnits(context: Context): ArrayList<UnitType> {
        var units: ArrayList<UnitType> = ArrayList<UnitType>()

        units.add(UnitType(context.getString(R.string.view_settings_temperature), "Kelvin", ""))
        units.add(UnitType(context.getString(R.string.view_settings_humidity), "Percentage", ""))
        units.add(UnitType(context.getString(R.string.view_settings_level_sea), "Hecto Pascal", ""))
        units.add(UnitType(context.getString(R.string.view_settings_level_ground), "Hecto Pascal", ""))
        units.add(UnitType(context.getString(R.string.view_settings_precipitation), "Hecto Pascal", ""))
        units.add(UnitType(context.getString(R.string.view_settings_feels_like), "Degree", ""))
        units.add(UnitType(context.getString(R.string.view_settings_cloudiness), "Percentage", ""))
        units.add(UnitType(context.getString(R.string.view_settings_wind), "Meter per Second", ""))

        return units
    }

    fun getDefaultUnit(context: Context): UnitType =
        UnitType(
            context.getString(R.string.unit_of_measurement),
            context.getString(R.string.unit_standard),
            "")
}