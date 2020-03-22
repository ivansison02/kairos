package com.ivansison.kairos.views.adapters

import android.content.Context
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
import com.ivansison.kairos.models.Main
import com.ivansison.kairos.models.WeatherDetail
import com.ivansison.kairos.views.activities.HomeActivity

class WeatherDetailAdapter(private val context: Context, private val parent: HomeActivity, private val items: ArrayList<WeatherDetail>) : RecyclerView.Adapter<WeatherDetailHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDetailHolder {
        return WeatherDetailHolder(LayoutInflater.from(context).inflate(R.layout.layout_weather_detail_item, parent, false))
    }

    override fun onBindViewHolder(holder: WeatherDetailHolder, position: Int) {
        val detail: WeatherDetail = items[position]
        holder.txtTitle.text = detail.title
        holder.txtValue.text = detail.value

        Glide.with(context).load(detail.image)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .into(holder.imgIcon)
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