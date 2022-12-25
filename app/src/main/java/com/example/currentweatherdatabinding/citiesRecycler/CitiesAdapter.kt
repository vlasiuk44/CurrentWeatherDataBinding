package com.example.currentweatherdatabinding.citiesRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currentweatherdatabinding.R

class CitiesAdapter(private val citiesList: Array<String>, private val context: Context) : RecyclerView.Adapter<CitiesAdapterItem>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesAdapterItem {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item, parent, false)
        return CitiesAdapterItem(itemView)
    }

    override fun onBindViewHolder(holder: CitiesAdapterItem, position: Int) {
        holder.cityName.text = citiesList[position]
    }

    override fun getItemCount(): Int {
        return citiesList.size
    }
}
