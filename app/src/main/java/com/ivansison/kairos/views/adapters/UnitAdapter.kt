package com.ivansison.kairos.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivansison.kairos.R
import com.ivansison.kairos.models.Unit
import com.ivansison.kairos.views.activities.SettingsActivity

class UnitAdapter(private val context: Context, private val parent: SettingsActivity, private val prefUnit: Unit, private val items: ArrayList<Unit>) : RecyclerView.Adapter<UnitHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitHolder {
        return UnitHolder(LayoutInflater.from(context).inflate(R.layout.layout_unit_item, parent, false))
    }

    override fun onBindViewHolder(holder: UnitHolder, position: Int) {
        val unit: Unit = items[position]
        holder.txtTitle.text = unit.title
        holder.txtValue.text = unit.value

        /*holder.itemView.setOnClickListener {
            parent.onShowMenu(unit)
        }*/
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class UnitHolder (view: View) : RecyclerView.ViewHolder(view) {
    val txtTitle: TextView = view.findViewById(R.id.txt_title)
    val txtValue: TextView = view.findViewById(R.id.txt_value)
}