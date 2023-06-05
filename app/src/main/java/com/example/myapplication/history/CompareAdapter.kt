package com.example.myapplication.history

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.CategoriesData

class CompareAdapter(val compareobjects: ArrayList<CompareObject>) : RecyclerView.Adapter<CompareAdapter.MyViewHolder>(){

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val categoryNameTV = itemView.findViewById<TextView>(R.id.titletv)
        val card1CV = itemView.findViewById<CardView>(R.id.cardResult1)
        val text1TV = itemView.findViewById<TextView>(R.id.textResult1)
        val card2CV = itemView.findViewById<CardView>(R.id.cardResult2)
        val text2TV = itemView.findViewById<TextView>(R.id.textResult2)

        val radius1TV = itemView.findViewById<TextView>(R.id.radius1)
        val radius2TV = itemView.findViewById<TextView>(R.id.radius2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompareAdapter.MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.compare_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return compareobjects.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = compareobjects[position]
        holder.categoryNameTV.text = currentItem.name

        val categoriesData = CategoriesData()
        if(currentItem.count1 == -1){
            holder.text1TV.text = "?"
            val color = Color.rgb(150, 150, 150)
            holder.card1CV.setCardBackgroundColor(color)
        } else{
            holder.text1TV.text = currentItem.count1.toString()
            holder.card1CV.setCardBackgroundColor(
                categoriesData.colorEvaluation(currentItem.count1, currentItem.maxCount)
            )
        }

        if(currentItem.count2 == -1){
            holder.text2TV.text = "?"
            val color = Color.rgb(150, 150, 150)
            holder.card2CV.setCardBackgroundColor(color)
        } else{
            holder.text2TV.text = currentItem.count2.toString()
            holder.card2CV.setCardBackgroundColor(
                categoriesData.colorEvaluation(currentItem.count2, currentItem.maxCount)
            )
        }

    }
}