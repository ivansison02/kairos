package com.ivansison.kairos.utils

import com.ivansison.kairos.models.Unit as UnitType

object DummyUtil {

    fun getUnits(): ArrayList<UnitType> {
        var units: ArrayList<UnitType> = ArrayList<UnitType>()

        units.add(UnitType("Temperature", "Kelvin", ""))
        units.add(UnitType("Wind", "Meter/Sec", ""))
        units.add(UnitType("Pressure", "hPa", ""))

        return units
    }

    fun getDefaultUnit(): UnitType =
        UnitType(
            "Unit of Measurement",
            "Standard",
            "")
}