package com.ivansison.kairos.views.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
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
import com.ivansison.kairos.views.adapters.WeatherTypeAdapter
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

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        title = ""

        mCache = CacheUtil(this)
        mDialog = DialogUtil(this, null)

        mUserPrefs = mCache?.getUserPreferences()

        lyt_error.visibility = View.GONE

        if (mCache!!.hasCache() && mUserPrefs?.isNew!!) getCurrentWeather(mUserPrefs?.location?.coordinates?.latitude.toString(), mUserPrefs?.location?.coordinates?.longitude.toString())
        else onManageLocationSearches()

        img_search.setOnClickListener {
            startActivityForResult(
                Intent(this, LocationActivity::class.java),
                LocationUtil.LOCATION_ID
            )
        }

        img_settings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        swipe_home.setOnRefreshListener {
            onManageLocationSearches()
        }
    }

    private fun onManageLocationSearches() {
        var location: Location? = null

        // Check if current location is most recent search or no recent search is existing, then display current location
        if (mCache?.getRecentSearches()?.isCurrentLocation!! ||
            mCache?.getRecentSearches()?.locations?.size == 0) location = mCache?.getUserPreferences()?.location!!
        else location = mCache?.getRecentSearches()!!.locations[0]

        getCurrentWeather("${location.address1},${location.country}")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LocationUtil.LOCATION_ID && resultCode == Activity.RESULT_OK) getCurrentWeather(data?.getStringExtra("q")!!)
        else if (requestCode == LocationUtil.LOCATION_ID && resultCode == Activity.RESULT_FIRST_USER) {
            mUserPrefs = mCache?.getUserPreferences()
            getCurrentWeather(mUserPrefs?.location?.coordinates?.latitude.toString(), mUserPrefs?.location?.coordinates?.longitude.toString())
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
        onStartLoading()
        OpenWeatherApi.getCurrentWeather(lat = latitude, lon = longitude, unit =  mUserPrefs?.prefUnit?.value!!)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    onValidateRequest("$latitude, $longitude", response)
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    lyt_error.visibility = View.VISIBLE
                    onShowDialog(ResponseUtil.getErrorMessage(t.toString()))
                }
            })
    }

    private fun getCurrentWeather(q: String) {
        onStartLoading()
        OpenWeatherApi.getCurrentWeather(q, mUserPrefs?.prefUnit?.value!!)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    onValidateRequest(q, response)
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    lyt_error.visibility = View.VISIBLE
                    onShowDialog(ResponseUtil.getErrorMessage(t.toString()))
                }
            })
    }

    private fun onValidateRequest(parameter: String, response: Response<WeatherResponse>) {
        clearDisplay()

        if (ResponseUtil.isSuccessful(response.code())) onFoundCurrentWeather(response.body()!!)
        else onShowDialog(ResponseUtil.getErrorMessage(response, parameter))
    }

    private fun clearDisplay() {
        txt_location.text = ""
        txt_degree.text = ""
        txt_unit.text = ""
        lyt_error.visibility = View.VISIBLE

        rcv_weather_type.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcv_weather_type.adapter = WeatherTypeAdapter(this, this, ArrayList())

        rcv_weather_detail.layoutManager = GridLayoutManager(this, 2)
        rcv_weather_detail.adapter = WeatherDetailAdapter(this, mUserPrefs?.prefUnit!!, ArrayList())
    }

    private fun onShowDialog(message: String) {
        if (swipe_home.isRefreshing) swipe_home.isRefreshing = false
        onStopLoading()
        mDialog?.onShowMessage(getString(R.string.message_title_notice), message, getString(R.string.message_ok),null, null)
    }

    private fun onFoundCurrentWeather(weather: WeatherResponse) {
        lyt_error.visibility = View.GONE

        if (swipe_home.isRefreshing) swipe_home.isRefreshing = false
        onStopLoading()

        if (weather.weatherType.size > 0) {
            val type = weather.weatherType[0]
            if (type.icon!!.contains("d")) {
                Glide.with(this).load(getDrawable(R.drawable.img_sun))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .into(img_mode)
                lyt_header.setBackgroundColor(resources.getColor(R.color.colorAccent))
            } else {
                Glide.with(this).load(getDrawable(R.drawable.img_moon))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .into(img_mode)
                lyt_header.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            }
        }

        val country: String? = weather.sys?.country
        val city = weather.name!!
        when (weather.sys?.country) {
            null -> txt_location.text = city
            else -> txt_location.text = getString(R.string.concat_city_country, city, country)
        }


        when (mUserPrefs?.prefUnit?.value!!) {
            getString(R.string.unit_standard) -> txt_unit.text = getString(R.string.symbol_kelvin)
            getString(R.string.unit_metric) -> txt_unit.text = getString(R.string.symbol_celsius)
            else -> getString(R.string.symbol_fahrenheit)
        }

        txt_degree.text = weather.details?.temp.toString()

        rcv_weather_type.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcv_weather_type.adapter = WeatherTypeAdapter(this, this, weather.weatherType)

        rcv_weather_detail.layoutManager = GridLayoutManager(this, 2)
        rcv_weather_detail.adapter = WeatherDetailAdapter(this,
            mUserPrefs?.prefUnit!!,
            WeatherDetailUtil.getDetails(
                this,
                weather.details,
                weather.clouds,
                weather.wind,
                weather.rain,
                weather.snow))

        // USER IS NEW, DISPLAYING CURRENT LOCATION
        if (mUserPrefs?.isNew!!) {
            mUserPrefs?.isNew = false
            mUserPrefs?.location?.country = country
            mUserPrefs?.location?.address1 = city
            mCache?.updateCache(mUserPrefs)

            setCurrentLocation()
        }
        // CURRENT LOCATION IS MOST RECENT SEARCH
        else if (hasSameLocation(country, city, mUserPrefs?.location?.country!!, mUserPrefs?.location?.address1!!)) {
            setCurrentLocation()
        }
        else  {
            val recentSearches: RecentSearches? = mCache?.getRecentSearches()
            recentSearches?.isCurrentLocation = false

            if (recentSearches?.locations!!.size == 0) {
                // NEW SEARCH
                recentSearches.locations.add(Location(weather.id.toLong(), country, city, "", weather.coord))
                onUpdateRecentSearches(recentSearches)
            }
            else {
                var newLocation: Location? = null
                for (location in recentSearches.locations) {
                    newLocation = Location(weather.id.toLong(), country, city, "", weather.coord)

                    val savedCountry = location.country
                    val savedCity = location.address1

                    if (hasSameLocation(country, city, savedCountry, savedCity)) {
                        // FOUND IN SAVED LOCATIONS
                        recentSearches.locations.remove(location)
                        break
                    }
                }

                recentSearches.locations.add(0, newLocation!!)

                val currentLocationSize = recentSearches.locations.size
                if (currentLocationSize > LocationUtil.MAX_SEARCHES) recentSearches.locations.removeAt(currentLocationSize - 1)

                onUpdateRecentSearches(recentSearches)
            }
        }
    }

    private fun hasSameLocation(newCountry: String?, newCity: String, savedCountry: String?, savedCity: String): Boolean {
        return newCountry == savedCountry && newCity == savedCity
    }

    private fun setCurrentLocation() {
        val recentSearches: RecentSearches? = mCache?.getRecentSearches()
        recentSearches?.isCurrentLocation = true
        onUpdateRecentSearches(recentSearches!!)
    }

    private fun onUpdateRecentSearches(recentSearches: RecentSearches) {
        mCache?.updateCache(recentSearches)
    }
}
