package com.example.myapplication.results.recyclerview_inner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Place
import com.example.myapplication.R

class PlacesAdapter (private val placesItems: ArrayList<Place>):
    RecyclerView.Adapter<PlacesAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameTV = itemView.findViewById<TextView>(R.id.placeName)
        val addressTV = itemView.findViewById<TextView>(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.place_card, parent, false))
    }

    override fun getItemCount(): Int {
        return placesItems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = placesItems[position]
        holder.nameTV.text = currentItem.name
        holder.addressTV.text = currentItem.address
    }

}