package com.ivansison.kairos.views.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ivansison.kairos.R
import com.ivansison.kairos.models.Coordinates
import com.ivansison.kairos.models.Location
import com.ivansison.kairos.models.RecentSearches
import com.ivansison.kairos.models.UserPreferences
import com.ivansison.kairos.utils.CacheUtil
import com.ivansison.kairos.utils.DialogUtil
import com.ivansison.kairos.utils.LocationUtil
import com.ivansison.kairos.utils.ValidateUtil
import com.ivansison.kairos.views.adapters.LocationAdapter
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.content_location.*

class LocationActivity : AppCompatActivity(), LocationUtil.LocationInterface {

    private var mCurrentLocation: Location? = null
    private var mRecentSearches: ArrayList<Location> = ArrayList()

    private var mCache: CacheUtil? = null
    private var mDialog: DialogUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        title = ""
        setSupportActionBar(toolbar_location)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fld_search.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (ValidateUtil.isValidLocation(fld_search.text.toString())) onGetWeather(fld_search.text.toString())
                else fld_search.error = getString(R.string.message_invalid_field)
                return@OnKeyListener true
            }
            false
        })

        mCache = CacheUtil(this)

        setupLocation()
        setupRecycler()
    }

    private fun setupLocation() {
        mCurrentLocation = mCache?.getUserPreferences()?.location

        val layout: View = findViewById(R.id.lyt_my_location)
        lyt_my_location.setOnClickListener {
            if (mCurrentLocation != null) onSelectedItem(mCurrentLocation!!)
        }

        val imgAction: ImageView = layout.findViewById(R.id.img_action)
        imgAction.setOnClickListener {
            // TODO: Get/Refresh location
            onStartLoading()
            LocationUtil(this, this).getLastLocation()
        }

        Glide.with(this).load(this.getDrawable(R.drawable.ic_refresh_2x))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .into(imgAction)

        val txtAddress1: TextView = layout.findViewById(R.id.txt_address1)
        if (mCurrentLocation != null) txtAddress1.text = mCurrentLocation?.address1
        else {
            txtAddress1.text = getString(R.string.view_location_get_current)
            Glide.with(this).load(this.getDrawable(R.drawable.ic_search))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(imgAction)
        }

        val txtAddress2: TextView = layout.findViewById(R.id.txt_address2)
        txtAddress2.text = mCurrentLocation?.country
    }

    private fun setupRecycler() {
        mRecentSearches = mCache?.getRecentSearches()?.locations!!

        rcv_location.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcv_location.adapter = LocationAdapter(this, this, mRecentSearches)

        onManageLocationResults()
    }

    private fun onStartLoading() {
        mDialog?.onShowLoading()
    }

    private fun onStopLoading() {
        mDialog?.onHideLoading()
    }

    private fun onManageLocationResults() {
        if (mRecentSearches.size == 0) lyt_no_avail.visibility = View.VISIBLE
        else lyt_no_avail.visibility = View.GONE
    }

    private fun onGetWeather(q: String) {
        setResult(Activity.RESULT_OK, Intent().putExtra("q", q))
        finish()
    }

    // MARK: - LocationAdapter
    fun onSelectedItem(location: Location) {
        onGetWeather("${location.address1},${location.country}")
    }

    fun onDeleteItem(location: Location) {
        mRecentSearches.remove(location)
        val recentSearches: RecentSearches = mCache?.getRecentSearches()!!
        recentSearches.locations = mRecentSearches
        mCache?.updateCache(recentSearches)
        onManageLocationResults()
    }

    override fun onFoundLocation(location: android.location.Location) {
        mCache?.updateCache(UserPreferences(true, Location(0, "", "", "", Coordinates(location.latitude, location.longitude)), ""))
        onStopLoading()

        setResult(Activity.RESULT_FIRST_USER, Intent())
        finish()
    }
}
