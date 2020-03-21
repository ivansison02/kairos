package com.ivansison.kairos.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import com.ivansison.kairos.R
import kotlinx.android.synthetic.main.activity_home.*

class DialogUtil(private var context: Context) {
    private var mDialog: Dialog? = null

    fun onShowLoading() {
        mDialog = Dialog(context)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog!!.setCancelable(false)
        mDialog!!.setContentView(R.layout.layout_loading)

        val imgLoading: ImageView = mDialog!!.findViewById(R.id.img_loading)

        Glide.with(context)
            .asGif()
            .load(R.raw.loading_kairos)
            .into(imgLoading)

        mDialog!!.show()
    }

    fun onHideLoading() {
        mDialog?.dismiss()
    }

    fun onShowMessage(title: String, message: String, positive: String?, negative: String?, cancel: String?) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle(title)
        builder.setMessage(message)

        if (positive != null) {
            builder.setPositiveButton(positive) {dialog, which ->

            }
        }

        if (negative != null) {
            builder.setNegativeButton(negative) {dialog, which ->

            }
        }

        if (cancel != null) {
            builder.setNeutralButton(cancel) {dialog, which ->

            }
        }

        mDialog = builder.create()
        mDialog!!.show()
    }

    fun onHideMessage() {
        mDialog!!.dismiss()
    }
}