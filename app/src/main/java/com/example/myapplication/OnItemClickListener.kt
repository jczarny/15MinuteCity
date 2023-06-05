package com.example.myapplication

import com.example.myapplication.data.database.SavedResult

interface OnItemClickListener {
    fun onItemClickListener(categoryTag: String)
    fun onItemClickListener(result: SavedResult, option: String)
}