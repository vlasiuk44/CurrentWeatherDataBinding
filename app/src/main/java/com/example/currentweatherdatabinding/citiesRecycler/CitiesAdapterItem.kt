package com.example.currentweatherdatabinding.citiesRecycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currentweatherdatabinding.R

class CitiesAdapterItem(view: View): RecyclerView.ViewHolder(view) {
    val cityName: TextView = view.findViewById(R.id.city_name)
}