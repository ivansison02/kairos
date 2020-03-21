package com.ivansison.kairos.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.ivansison.kairos.models.Location
import com.ivansison.kairos.models.RecentSearches
import com.ivansison.kairos.models.UserPreferences

class CacheUtil(val context: Context) {

    private val mShared: SharedPreferences
    private val mEditor: SharedPreferences.Editor

    companion object {
        private const val PREF_NAME = "prefs"
        private const val KEY_PREFERENCES = "user_prefs"
        private const val KEY_LOCATIONS = "user_locations"
    }

    init {
        mShared = activity.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
        mEditor = mShared.edit()
    }

    val activity: Activity
        get() = context as Activity

    fun getUserPreferences(): UserPreferences? {
        val account = mShared.getString(KEY_PREFERENCES, "")
        return Gson().fromJson(account, UserPreferences::class.java)
    }

    fun getRecentSearches(): RecentSearches? {
        val account = mShared.getString(KEY_LOCATIONS, "")
        return Gson().fromJson(account, RecentSearches::class.java)
    }

    fun hasCache(): Boolean {
        val pref = mShared.getString(KEY_PREFERENCES, "")
        val userPrefs: UserPreferences? = Gson().fromJson(pref, UserPreferences::class.java)
        return userPrefs != null
    }

    fun updateCache(account: UserPreferences?) {
        mEditor.putString(KEY_PREFERENCES, Gson().toJson(account))
        mEditor.apply()
    }

    fun updateCache(locations: RecentSearches) {
        mEditor.putString(KEY_LOCATIONS, Gson().toJson(locations))
        mEditor.apply()
    }
}
