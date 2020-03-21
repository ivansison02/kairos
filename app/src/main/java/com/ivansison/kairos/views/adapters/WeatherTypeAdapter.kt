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
import com.ivansison.kairos.controllers.apis.OpenWeatherApi
import com.ivansison.kairos.models.WeatherType
import com.ivansison.kairos.views.activities.HomeActivity

class WeatherTypeAdapter(val context: Context, val parent: HomeActivity, val items: ArrayList<WeatherType>) : RecyclerView.Adapter<WeatherTypeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherTypeHolder {
        return WeatherTypeHolder(LayoutInflater.from(context).inflate(R.layout.layout_weather_type_item, parent, false))
    }

    override fun onBindViewHolder(holder: WeatherTypeHolder, position: Int) {
        val type: WeatherType = items.get(position)
        holder.txtWeather.text = type.description!!.capitalize()

        Glide.with(context).load( "${OpenWeatherApi.URL_ICON}${type.icon!!}${OpenWeatherApi.ICON_SIZE}")
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .apply(RequestOptions().error(R.drawable.ic_image_broken))
            .into(holder.imgWeather)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class WeatherTypeHolder (view: View) : RecyclerView.ViewHolder(view) {
    val txtWeather: TextView = view.findViewById(R.id.txt_weather)
    val imgWeather: ImageView = view.findViewById(R.id.img_weather)
}