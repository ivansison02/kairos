package com.ivansison.kairos.views.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.ivansison.kairos.R
import com.ivansison.kairos.models.Coordinates
import com.ivansison.kairos.models.RecentSearches
import com.ivansison.kairos.models.UserPreferences
import com.ivansison.kairos.utils.CacheUtil
import com.ivansison.kairos.utils.DialogUtil
import com.ivansison.kairos.utils.PermissionUtil


class MainActivity : AppCompatActivity() {

    private val mDuration: Long = 4000

    private lateinit var mLocation: Location
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private var mCache: CacheUtil? = null
    private var mLoadingDialog: DialogUtil? = null

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            mLocation = locationResult.lastLocation
            onManageCache()
            onStartAnimation()
        }
    }

    private fun onManageCache() {
        mCache?.updateCache(
            UserPreferences(true, com.ivansison.kairos.models.Location(0, "", "", "", Coordinates(mLocation.latitude, mLocation.longitude)), ""))
        mCache?.updateCache(RecentSearches())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCache = CacheUtil(this)
        mLoadingDialog = DialogUtil(this)

        if (mCache!!.hasCache()) onStartAnimation()
        else getLastLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PermissionUtil.PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    private fun onStartLoading() {
        mLoadingDialog?.onShowLoading()
    }

    private fun onStopLoading() {
        mLoadingDialog?.onHideLoading()
    }

    private fun onStartAnimation() {
        onStopLoading()

        Handler().postDelayed(Runnable {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, mDuration)
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (PermissionUtil.checkPermissions(this)) {
            onStartLoading()

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            mLocation = location
                            onManageCache()
                            onStartAnimation()
                        }
                    }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            PermissionUtil.requestPermissions(this)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
}
