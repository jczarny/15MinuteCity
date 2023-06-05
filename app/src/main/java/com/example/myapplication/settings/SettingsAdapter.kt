package com.example.myapplication.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class SettingsAdapter (private val onItemClickListener: com.example.myapplication.OnItemClickListener):
    RecyclerView.Adapter<SettingsAdapter.MyViewHolder>() {

    private var categoriesList = arrayListOf<CategorySetting>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameTV = itemView.findViewById<TextView>(R.id.catName)
        val iconIV = itemView.findViewById<ImageView>(R.id.catIcon)
        val checkbox = itemView.findViewById<CheckBox>(R.id.checkbox)
        val layout = itemView.findViewById<LinearLayout>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.settings_category_card, parent, false))
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = categoriesList[position]
        holder.nameTV.text = currentItem.name
        holder.iconIV.setImageResource(currentItem.image)
        holder.checkbox.isChecked = currentItem.isChecked

        holder.layout.setOnClickListener {
            categoriesList[position].isChecked = !categoriesList[position].isChecked
            holder.checkbox.isChecked = !holder.checkbox.isChecked
            onItemClickListener.onItemClickListener(currentItem.tag)
        }
    }

    fun setData(categories: ArrayList<CategorySetting>){
        this.categoriesList = categories
        notifyDataSetChanged()
    }

}