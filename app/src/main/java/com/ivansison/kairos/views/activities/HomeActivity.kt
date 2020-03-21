package com.ivansison.kairos.views.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ivansison.kairos.R
import com.ivansison.kairos.controllers.apis.OpenWeatherApi
import com.ivansison.kairos.controllers.responses.WeatherResponse
import com.ivansison.kairos.models.Location
import com.ivansison.kairos.models.RecentSearches
import com.ivansison.kairos.models.UserPreferences
import com.ivansison.kairos.utils.*
import com.ivansison.kairos.views.adapters.WeatherDetailAdapter
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {

    private var mUserPrefs: UserPreferences? = null

    private var mCache: CacheUtil? = null
    private var mDialog: DialogUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        title = ""

        mCache = CacheUtil(this)
        mDialog = DialogUtil(this)

        mUserPrefs = mCache?.getUserPreferences()

        if (mCache!!.hasCache() && mUserPrefs?.isNew!!) {
            getCurrentWeather(mUserPrefs?.location?.coordinates?.latitude.toString(),
                mUserPrefs?.location?.coordinates?.longitude.toString()
            )
        } else {
            // TODO: Display most recent search
            var location: Location? = null

            // Check if current location is most recent search or no recent search is existing, then display current location
            Log.i(ConstantUtil.TAG, "CURRENT LOCATION IS MOST RECENT?: ${mCache?.getRecentSearches()?.isCurrentLocation!!}")
            if (mCache?.getRecentSearches()?.isCurrentLocation!! ||
                mCache?.getRecentSearches()?.locations?.size == 0) location = mCache?.getUserPreferences()?.location!!
            else location = mCache?.getRecentSearches()!!.locations[0]

            getCurrentWeather("${location.address1},${location.country}")
        }

        img_search.setOnClickListener {
            startActivityForResult(
                Intent(this, LocationActivity::class.java),
                LocationUtil.LOCATION_ID
            )
        }

        img_settings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LocationUtil.LOCATION_ID && resultCode == Activity.RESULT_OK) {
            getCurrentWeather(data?.getStringExtra("q")!!)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onStartLoading() {
        mDialog?.onShowLoading()
    }

    private fun onStopLoading() {
        mDialog?.onHideLoading()
    }

    private fun getCurrentWeather(latitude: String, longitude: String) {
        Log.i(ConstantUtil.TAG, "Getting weather of current location")
        OpenWeatherApi.getCurrentWeather(lat = latitude, lon = longitude)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    onValidateRequest("$latitude, $longitude", response)
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    onShowDialog(ResponseUtil.getErrorMessage(t.toString()))
                }
            })
    }

    private fun getCurrentWeather(q: String) {
        onStartLoading()
        Log.i(ConstantUtil.TAG, "Getting weather of current location by city, zip")
        OpenWeatherApi.getCurrentWeather(q)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    onValidateRequest(q, response)
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    onShowDialog(ResponseUtil.getErrorMessage(t.toString()))
                }
            })
    }

    private fun onValidateRequest(parameter: String, response: Response<WeatherResponse>) {
        Log.e(ConstantUtil.TAG, response.toString())
        if (ResponseUtil.isSuccessful(response.code())) onFoundCurrentWeather(response.body()!!)
       else onShowDialog(ResponseUtil.getErrorMessage(response, parameter))
    }

    private fun onShowDialog(message: String) {
        onStopLoading()

        mDialog?.onShowMessage(getString(R.string.message_title_notice), message, getString(R.string.message_ok),null, null)
    }

    private fun onFoundCurrentWeather(weather: WeatherResponse) {
        onStopLoading()

        rcv_weather.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcv_weather.adapter = WeatherDetailAdapter(this, this, weather.weatherType)

        if (weather.weatherType.size > 0) {
            val type = weather.weatherType[0]
            if (type.icon!!.contains("d")) {
                Glide.with(this).load(getDrawable(R.drawable.img_sun))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .into(img_mode)
                toolbar.setBackgroundColor(resources.getColor(R.color.colorAccent))
            } else {
                Glide.with(this).load(getDrawable(R.drawable.img_moon))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .into(img_mode)
                toolbar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            }
        }

        txt_degree.text = weather.weatherDetails?.temp.toString()
        txt_location.text = weather.name

        val country = weather.sys?.country!!
        val city = weather.name!!

        if (mUserPrefs?.isNew!!) {
            mUserPrefs?.isNew = false
            mUserPrefs?.location?.country = country
            mUserPrefs?.location?.address1 = city
            mCache?.updateCache(mUserPrefs)
        }
        else if (hasSameLocation(country, city, mUserPrefs?.location?.country!!, mUserPrefs?.location?.address1!!)) {
            Log.i(ConstantUtil.TAG, "SET CURRENT LOCATION TO MOST RECENT SEARCHES")
            val recentSearches: RecentSearches? = mCache?.getRecentSearches()
            recentSearches?.isCurrentLocation = true
            onUpdateRecentSearches(recentSearches!!)
        }
        else  {
            val recentSearches: RecentSearches? = mCache?.getRecentSearches()
            recentSearches?.isCurrentLocation = false

            if (recentSearches?.locations!!.size == 0) {
                Log.i(ConstantUtil.TAG, "NEW LOCATION")
                recentSearches.locations.add(Location(weather.id.toLong(), country, city, "", weather.coord))
                onUpdateRecentSearches(recentSearches)
            }
            else {
                var newLocation: Location? = null
                var hasLocation = false
                for (location in recentSearches.locations) {
                    newLocation = Location(weather.id.toLong(), country, city, "", weather.coord)

                    val savedCountry = location.country
                    val savedCity = location.address1

                    Log.i(ConstantUtil.TAG, "Checking location: ${location.id}}")

                    if (hasSameLocation(country, city, savedCountry, savedCity)) {
                        Log.i(ConstantUtil.TAG, "SAME LOCATION: ${country}, ${city}")
                        recentSearches.locations.remove(location)
                        hasLocation = true
                        break
                    }
                }

                if (!hasLocation) {
                    Log.i(ConstantUtil.TAG, "LOCATION NOT YET SAVED")
                    recentSearches.locations.add(0, newLocation!!)
                    onUpdateRecentSearches(recentSearches)
                }
                else {
                    recentSearches.locations.add(0, newLocation!!)
                    onUpdateRecentSearches(recentSearches)
                }
            }
        }
    }

    private fun hasSameLocation(newCountry: String, newCity: String, savedCountry: String, savedCity: String): Boolean {
        return newCountry == savedCountry && newCity == savedCity
    }

    private fun onUpdateRecentSearches(recentSearches: RecentSearches) {
        mCache?.updateCache(recentSearches)
    }
}
