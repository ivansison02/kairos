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
import com.ivansison.kairos.models.Location
import com.ivansison.kairos.views.activities.LocationActivity

class LocationAdapter(val context: Context, val parent: LocationActivity, val items: ArrayList<Location>) : RecyclerView.Adapter<LocationHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {
        return LocationHolder(LayoutInflater.from(context).inflate(R.layout.layout_location_item, parent, false))
    }

    override fun onBindViewHolder(holder: LocationHolder, position: Int) {
        val location: Location = items.get(position)
        holder.txtAddress1.text = location.address1
        holder.txtAddress2.text = location.country
        Glide.with(context).load(context.getDrawable(R.drawable.ic_remove_2x))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .into(holder.imgAction)

        holder.imgAction.setOnClickListener {
            parent.onDeleteItem(location)
            notifyDataSetChanged()
        }
        holder.itemView.setOnClickListener {
            parent.onSelectedItem(location)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class LocationHolder (view: View) : RecyclerView.ViewHolder(view) {
    val txtAddress1: TextView = view.findViewById(R.id.txt_address1)
    val txtAddress2: TextView = view.findViewById(R.id.txt_address2)
    val imgAction: ImageView = view.findViewById(R.id.img_action)
}