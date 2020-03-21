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
import com.ivansison.kairos.utils.LocationUtil
import com.ivansison.kairos.utils.PermissionUtil


class MainActivity : AppCompatActivity(), LocationUtil.LocationInterface {

    private val mDuration: Long = 4000

    private var mCache: CacheUtil? = null
    private var mLoadingDialog: DialogUtil? = null
    private var mLocationUtil: LocationUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCache = CacheUtil(this)
        mLoadingDialog = DialogUtil(this)
        mLocationUtil = LocationUtil(this, this)

        if (mCache!!.hasCache()) onStartAnimation()
        else getLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PermissionUtil.PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    private fun getLocation() {
        onStartLoading()
        mLocationUtil?.getLastLocation()
    }

    private fun onStartLoading() {
        mLoadingDialog?.onShowLoading()
    }

    private fun onStopLoading() {
        mLoadingDialog?.onHideLoading()
    }

    private fun onStartAnimation() {
        Handler().postDelayed(Runnable {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, mDuration)
    }

    // MARK: - LocationInterface
    override fun onFoundLocation(location: Location) {
        mCache?.updateCache(UserPreferences(true, com.ivansison.kairos.models.Location(0, "", "", "", Coordinates(location.latitude, location.longitude)), ""))
        mCache?.updateCache(RecentSearches())

        onStopLoading()
        onStartAnimation()
    }
}
