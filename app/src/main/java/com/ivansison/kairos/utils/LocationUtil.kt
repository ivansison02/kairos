package com.ivansison.kairos.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import com.google.android.gms.location.*

class LocationUtil(var context: Context, var delegate: LocationInterface) {

    companion object {
        const val LOCATION_ID = 20
    }

    interface LocationInterface {
        fun onFoundLocation(location: Location)
    }

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            delegate.onFoundLocation(locationResult.lastLocation)
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER                                                                                                                                                                                                                               )
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if (PermissionUtil.checkPermissions(context)) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        if (location == null) requestNewLocationData()
                        else delegate.onFoundLocation(location)
                    }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
        } else PermissionUtil.requestPermissions(context as Activity)
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
}