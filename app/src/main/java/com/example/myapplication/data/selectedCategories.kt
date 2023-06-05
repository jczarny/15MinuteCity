package com.example.myapplication.data

import android.graphics.Color
import com.example.myapplication.R
import java.lang.Integer.max
import java.lang.Math.min

class CategoriesData {
    val allCategories = arrayListOf(
        Categories.ATM, Categories.BANK, Categories.RESTAURANT,
        Categories.BAR, Categories.CAFE, Categories.MOVIE_THEATER, Categories.GYM, Categories.LIBRARY, Categories.PARK,
        Categories.POST_OFFICE, Categories.CHURCH, Categories.DOCTOR, Categories.DRUGSTORE, Categories.PHARMACY,
        Categories.HOSPITAL, Categories.PRIMARY_SCHOOL, Categories.SECONDARY_SCHOOL, Categories.UNIVERSITY,
        Categories.SHOPPING_MALL, Categories.CONVENIENCE_STORE, Categories.SUPERMARKET,
        Categories.BUS_STATION, Categories.TRAIN_STATION)
    val allCategoriesIcons = arrayListOf(
        R.drawable.baseline_atm_24, R.drawable.baseline_atm_24, R.drawable.baseline_restaurant_24,
        R.drawable.baseline_bar_24, R.drawable.baseline_cafe_24, R.drawable.baseline_movie_24, R.drawable.baseline_gym_24, R.drawable.baseline_book_24, R.drawable.baseline_park_24,
        R.drawable.baseline_post_office_24, R.drawable.baseline_church_24, R.drawable.baseline_hospital_24, R.drawable.baseline_pharmacy_24, R.drawable.baseline_pharmacy_24,
        R.drawable.baseline_hospital_24, R.drawable.baseline_school_24, R.drawable.baseline_school_24, R.drawable.baseline_school_24,
        R.drawable.baseline_shopping_mall_24, R.drawable.baseline_convenience_store_24, R.drawable.baseline_convenience_store_24,
        R.drawable.baseline_bus_24, R.drawable.baseline_train_24)
    val allCategoriesNames = arrayListOf(
        "ATM", "Bank", "Restraurant",
        "Post", "Cafe", "Cinema & Theater", "Gym", "Library", "Park",
        "Post", "Church", "Doctor", "Drugstore", "Pharmacy",
        "Hospital", "Primary school", "Secondary school", "University",
        "Shopping mall", "Convenience store", "Supermarket",
        "Bus station", "Train station")
    val allCategoriesRequiredAmounts = arrayListOf(
        1, 1, 5,
        5, 3, 2, 1, 1, 3,
        1, 2, 2, 1, 1,
        1, 1, 1, 1,
        1, 5, 5,
        1, 1)

    fun getNameByTag(tag: String): String{
        val i = allCategories.indexOf(tag)
        if(i == -1 || i >= allCategoriesNames.size)
            throw Exception("Tag not found")
        else
            return allCategoriesNames[i]
    }

    fun getTagByName(name: String): String{
        val i = allCategoriesNames.indexOf(name)
        if(i == -1 || i >= allCategories.size)
            throw Exception("Name not found")
        else
            return allCategories[i]
    }

    fun getIconByName(name: String): Int{
        val i = allCategoriesNames.indexOf(name)
        if(i == -1 || i >= allCategoriesIcons.size)
            throw Exception("Name not found")
        else
            return allCategoriesIcons[i]
    }

    fun evaluate(results: ArrayList<CategoryItem>) : Pair<Int, Int>{
        var maxScore = 0
        var totalScore = 0
        for(category in results){
            val i = allCategoriesNames.indexOf(category.categoryName)
            maxScore += allCategoriesRequiredAmounts[i]
            totalScore += min(category.placesItems.size, allCategoriesRequiredAmounts[i])
        }

        return totalScore to maxScore
    }

    fun colorEvaluation(totalScore: Int, maxScore: Int): Int{
        var p = totalScore.toFloat() / maxScore.toFloat()
        p = p.coerceIn(0.2f, 0.8f)
        val red = ((1-p) * 255).toInt()
        val green = (p*255).toInt()
        val blue = 0
        return Color.rgb(red, green, blue)
    }
}