package com.ivansison.kairos.models

class UserPreferences(
    var isNew: Boolean,
    var location: Location?,
    var prefUnit: Unit,
    var units: ArrayList<Unit>
)