package com.ivansison.kairos.utils

import android.content.Context
import com.ivansison.kairos.R
import com.ivansison.kairos.models.Unit

object UnitUtil {
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

    fun getUnitValue(context: Context, prefUnit: Unit, unit: Unit): String  {
        return when (unit.title) {
            context.getString(R.string.view_settings_temperature),
            context.getString(R.string.view_settings_temperature_min),
            context.getString(R.string.view_settings_temperature_max) -> when (prefUnit.value) {
                context.getString(R.string.unit_standard) -> context.getString(R.string.unit_kelvin)
                context.getString(R.string.unit_metric) -> context.getString(R.string.unit_celsius)
                else -> context.getString(R.string.unit_fahrenheit)
            }
            context.getString(R.string.view_settings_pressure),
            context.getString(R.string.view_settings_level_sea),
            context.getString(R.string.view_settings_level_ground) -> context.getString(R.string.unit_pascal_hecto)
            context.getString(R.string.view_settings_precipitation),
            context.getString(R.string.view_settings_snow) -> context.getString(R.string.unit_millimeter)
            context.getString(R.string.view_settings_cloudiness),
            context.getString(R.string.view_settings_humidity) -> context.getString(R.string.unit_percentage)
            context.getString(R.string.view_settings_feels_like) -> context.getString(R.string.unit_degree)
            else ->  when (prefUnit.value) {
                context.getString(R.string.unit_standard), context.getString(R.string.unit_metric) -> context.getString(R.string.unit_meter_per_second)
                else -> context.getString(R.string.unit_miles_per_hour)
            }
        }
    }

    fun getUnitValueSymbol(context: Context, prefUnit: Unit, unit: Unit): String {
        return when (unit.title) {
            context.getString(R.string.view_settings_temperature),
            context.getString(R.string.view_settings_temperature_min),
            context.getString(R.string.view_settings_temperature_max) -> when (prefUnit.value) {
                    context.getString(R.string.unit_standard) -> context.getString(R.string.symbol_kelvin)
                    context.getString(R.string.unit_metric) -> context.getString(R.string.symbol_celsius)
                     else -> context.getString(R.string.symbol_fahrenheit)
            }
            context.getString(R.string.view_settings_pressure),
            context.getString(R.string.view_settings_level_sea),
            context.getString(R.string.view_settings_level_ground) -> context.getString(R.string.symbol_pascal_hecto)
            context.getString(R.string.view_settings_precipitation),
            context.getString(R.string.view_settings_snow) -> context.getString(R.string.symbol_millimeter)
            context.getString(R.string.view_settings_cloudiness),
            context.getString(R.string.view_settings_humidity) -> context.getString(R.string.symbol_percentage)
            context.getString(R.string.view_settings_feels_like) -> context.getString(R.string.symbol_degree)
            else ->  when (prefUnit.value) {
                context.getString(R.string.unit_standard), context.getString(R.string.unit_metric) -> context.getString(R.string.symbol_meter_per_second)
                else -> context.getString(R.string.symbol_miles_per_hour)
            }
        }
    }
}