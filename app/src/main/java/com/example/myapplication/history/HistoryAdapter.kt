package com.example.myapplication.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.CategoriesData
import com.example.myapplication.data.database.SavedResult

class HistoryAdapter (private val onItemClickListener: com.example.myapplication.OnItemClickListener):
    RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    private var historyResults = arrayListOf<SavedResult>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ratingTV = itemView.findViewById<TextView>(R.id.rating)
        val addressTV = itemView.findViewById<TextView>(R.id.address)
        val checkbox = itemView.findViewById<CheckBox>(R.id.checkbox)
        val layout = itemView.findViewById<LinearLayout>(R.id.inner_ll)
        val ratingCard = itemView.findViewById<CardView>(R.id.ratingCard)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.MyViewHolder {
        return HistoryAdapter.MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.history_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return historyResults.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = historyResults[position]
        holder.ratingTV.text = currentItem.rating.toString()
        holder.addressTV.text = currentItem.address.toString()

        holder.layout.setOnClickListener {
            onItemClickListener.onItemClickListener(currentItem, "CHECK")
        }
        holder.ratingTV.setOnClickListener {
            onItemClickListener.onItemClickListener(currentItem, "CHECK")
        }
        holder.addressTV.setOnClickListener {
            onItemClickListener.onItemClickListener(currentItem, "CHECK")
        }

        val categoriesData = CategoriesData()
        holder.ratingCard.setCardBackgroundColor(
            categoriesData.colorEvaluation(currentItem.rating, currentItem.maxRating)
        )
        holder.checkbox.setOnCheckedChangeListener{ buttonView, isChecked ->
            onItemClickListener.onItemClickListener(currentItem, "COMPARE")
        }
    }

    fun setData(results: ArrayList<SavedResult>){
        this.historyResults = results
        notifyDataSetChanged()
    }
}