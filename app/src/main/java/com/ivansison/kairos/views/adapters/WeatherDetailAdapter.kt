package com.ivansison.kairos.views.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ivansison.kairos.R
import com.ivansison.kairos.models.WeatherDetails
import com.ivansison.kairos.views.activities.HomeActivity

class WeatherDetailAdapter(val context: Context, val parent: HomeActivity, val items: ArrayList<WeatherDetails>) : RecyclerView.Adapter<WeatherDetailHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDetailHolder {
        return WeatherDetailHolder(LayoutInflater.from(context).inflate(R.layout.layout_weather_detail_item, parent, false))
    }

    override fun onBindViewHolder(holder: WeatherDetailHolder, position: Int) {
        val details: WeatherDetails = items.get(position)

    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class WeatherDetailHolder (view: View) : RecyclerView.ViewHolder(view) {
    val txtTitle: TextView = view.findViewById(R.id.txt_title)
    val txtValue: TextView = view.findViewById(R.id.txt_value)
    val imgIcon: ImageView = view.findViewById(R.id.img_icon)
}