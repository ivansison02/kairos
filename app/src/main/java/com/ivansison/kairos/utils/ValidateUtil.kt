package com.ivansison.kairos.utils

object ValidateUtil {

    fun isValidLocation(location: String) = when {
        location.isEmpty() -> false
        else -> true
    }
}