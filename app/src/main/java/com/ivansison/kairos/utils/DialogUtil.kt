package com.ivansison.kairos.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ivansison.kairos.R
import com.ivansison.kairos.models.Unit


class DialogUtil(private var context: Context, private var delegate: CustomDialogInterface?) {

    private var mDialog: Dialog? = null

    interface CustomDialogInterface {
        fun onSelectedItem(unit: Unit, index: Int)
        fun onClickedPositive(message: String)
    }

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
        builder.setCancelable(false)

        if (positive != null) {
            builder.setPositiveButton(positive) {dialog, which ->
                delegate?.onClickedPositive(message)
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

    fun onShowMenu(unit: Unit, menus: Array<String>) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(unit.title)
        builder.setItems(menus, DialogInterface.OnClickListener { dialog, which ->
            delegate?.onSelectedItem(unit, which)
        })
        builder.show()
    }
}