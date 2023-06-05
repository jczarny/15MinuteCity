package com.example.myapplication.results.recyclerview_outer

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.CategoryItem
import com.example.myapplication.R
import com.example.myapplication.data.CategoriesData
import com.example.myapplication.data.Tags
import com.example.myapplication.results.recyclerview_inner.PlacesAdapter

class CategoriesAdapter(private val onItemClickListener: com.example.myapplication  .OnItemClickListener):
    RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {

    private var categoriesList = arrayListOf<CategoryItem>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameTV = itemView.findViewById<TextView>(R.id.catName)
        val sizeTV = itemView.findViewById<TextView>(R.id.placesFound)
        val iconIV = itemView.findViewById<ImageView>(R.id.catIcon)
        val placesRecyclerView = itemView.findViewById<RecyclerView>(R.id.placesRecyclerview)
        val layoutView = itemView.findViewById<LinearLayout>(R.id.linearLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_card, parent, false))
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = categoriesList[position]
        holder.nameTV.text = currentItem.categoryName
        holder.sizeTV.text = currentItem.placesItems.size.toString()
        holder.placesRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        val icon = CategoriesData().getIconByName(currentItem.categoryName)
        holder.iconIV.setImageResource(icon)

        val adapter = PlacesAdapter(currentItem.placesItems)
        holder.placesRecyclerView.adapter = adapter

        holder.layoutView.setOnClickListener{
            if(holder.placesRecyclerView.visibility == View.VISIBLE)
                holder.placesRecyclerView.visibility = View.GONE
            else
                holder.placesRecyclerView.visibility = View.VISIBLE
        }
    }

    fun setData(categories: ArrayList<CategoryItem>){
        this.categoriesList = categories
        notifyDataSetChanged()
    }

}