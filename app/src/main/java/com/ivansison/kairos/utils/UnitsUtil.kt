package com.ivansison.kairos.utils

import android.content.Context
import com.ivansison.kairos.R
import com.ivansison.kairos.models.Unit

object UnitsUtil {
    fun getUnits(context: Context, unit: Unit): Array<String> = when (unit.title) {
        context.getString(R.string.view_settings_temperature) ->  context.resources.getStringArray(R.array.units_temperature)
        context.getString(R.string.view_settings_wind) ->  context.resources.getStringArray(R.array.units_wind_speed)
        context.getString(R.string.view_settings_pressure) ->  context.resources.getStringArray(R.array.units_pressure)
        else ->  context.resources.getStringArray(R.array.units)

    }

    fun getUnitValue(context: Context, unit: Unit, index: Int): String = when (unit.title) {
        context.getString(R.string.view_settings_temperature) -> context.resources.getStringArray(R.array.units_temperature)[index]
        context.getString(R.string.view_settings_wind) -> context.resources.getStringArray(R.array.units_wind_speed)[index]
        context.getString(R.string.view_settings_pressure) ->  context.resources.getStringArray(R.array.units_pressure)[index]
        else -> context.resources.getStringArray(R.array.units)[index]
    }

    fun getUnitValue(context: Context, prefUnit: Unit, savedUnit: Unit): String  {
        return if (savedUnit.title == context.getString(R.string.view_settings_temperature)) {
            if (prefUnit.value == context.getString(R.string.unit_standard)) context.getString(R.string.unit_kelvin)
            else if (prefUnit.value == context.getString(R.string.unit_metric)) context.getString(R.string.unit_celsius)
            else context.getString(R.string.unit_fahrenheit)
        }
        else if (savedUnit.title == context.getString(R.string.view_settings_pressure)) context.getString(R.string.unit_pascal_hecto)
        else {
            if (prefUnit.value == context.getString(R.string.unit_standard) || prefUnit.value == context.getString(R.string.unit_metric)) context.getString(R.string.unit_meter_per_second)
            else context.getString(R.string.unit_miles_per_hour)
        }
    }
}