package com.ivansison.kairos.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ivansison.kairos.R
import com.ivansison.kairos.models.Unit
import com.ivansison.kairos.models.UserPreferences
import com.ivansison.kairos.utils.CacheUtil
import com.ivansison.kairos.utils.ConstantUtil
import com.ivansison.kairos.utils.DialogUtil
import com.ivansison.kairos.utils.UnitsUtil
import com.ivansison.kairos.views.adapters.UnitAdapter
import kotlinx.android.synthetic.main.content_settings.*


class SettingsActivity : AppCompatActivity(), DialogUtil.CustomDialogInterface {

    private var mUserPrefs: UserPreferences? = null

    private var mPrefUnit: Unit? = null
    private var mUnits: ArrayList<Unit> = ArrayList()

    private var mDialog: DialogUtil? = null
    private var mCache: CacheUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mDialog = DialogUtil(this, this)
        mCache = CacheUtil(this)

        setupPrefs()
    }

    private fun setupPrefs() {
        mUserPrefs = mCache?.getUserPreferences()
        mPrefUnit = mUserPrefs?.prefUnit
        mUnits = mUserPrefs!!.units

        val viewUnit: View = findViewById(R.id.lyt_unit)
        viewUnit.setOnClickListener {
            onShowMenu(mUserPrefs?.prefUnit!!)
        }

        val txtTitle: TextView = viewUnit.findViewById(R.id.txt_title)
        txtTitle.text = mPrefUnit?.title

        val txtValue: TextView = viewUnit.findViewById(R.id.txt_value)
        txtValue.text = getString(R.string.concat_selected_value, mPrefUnit?.value)

        rcv_settings.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcv_settings.adapter = UnitAdapter(this, this, mPrefUnit!!, mUnits)
    }

    private fun onShowMenu(unit: Unit) {
        val items: Array<String> = UnitsUtil.getUnits(this, unit)
        mDialog?.onShowMenu(unit, items)
    }

    // MARK: - DialogInterface
    override fun onSelectedItem(unit: Unit, index: Int) {
        val newValue = UnitsUtil.getUnitValue(this, unit, index)
        Log.e(ConstantUtil.TAG, "COMPARE: ${mUserPrefs?.prefUnit?.value}, $newValue")

        if (mUserPrefs?.prefUnit?.value != newValue) {
            unit.value = newValue
            mUserPrefs?.prefUnit = unit

            // THIS IS FOR MEASUREMENT SYSTEM
            for (currentUnit in mUnits.withIndex()) {
                Log.e(ConstantUtil.TAG, "UNIT: ${currentUnit.value.title}, ${currentUnit.value.value}")
                currentUnit.value.value = UnitsUtil.getUnitValue(context = this, prefUnit = unit, savedUnit = currentUnit.value)
            }

            // THIS IS FOR INDIVIDUAL UNIT PREFERENCE
            /*for (newUnit in mUnits.withIndex()) {
                Log.e(ConstantUtil.TAG, "COMPARING: ${newUnit.value.title}, ${newUnit.value.value} - ${unit.title}, ${unit.value}")
                if (newUnit.value.title == unit.title) mUnits[newUnit.index] = unit
                break
            }*/

            mCache?.updateCache(mUserPrefs)

            // Upon clicking OK, restart activity to make the changes.
            mDialog?.onShowMessage(
                getString(R.string.message_title_success),
                getString(R.string.message_success_changes),
                getString(R.string.message_ok),
                null,
                null)
        }
    }

    override fun onClickedPositive(message: String) {
        if (message == getString(R.string.message_success_changes)) {
            startActivity(Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK))
            finish()
        }
    }
}
