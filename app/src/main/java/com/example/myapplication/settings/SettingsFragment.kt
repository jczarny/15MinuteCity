package com.example.myapplication.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.*
import com.example.myapplication.data.Categories
import com.example.myapplication.data.CategoriesData
import com.example.myapplication.data.database.SavedResult
import com.example.myapplication.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var textview: TextView

    private lateinit var adapter: SettingsAdapter

    private var categoriesSettings: ArrayList<CategorySetting> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillCategoriesSettings()
        this.adapter = SettingsAdapter(this)
        this.adapter.setData(categoriesSettings)
        binding.categoriesRv.layoutManager = LinearLayoutManager(context)
        binding.categoriesRv.adapter = this.adapter

        textview = requireActivity().findViewById(R.id.radiusTV)
        initliazeSlider()
    }

    private fun initliazeSlider(){
        val initialRadius = (activity as SettingsActivity).selectedRadius.toFloat()
        binding.radiusSlider.value = initialRadius
        textview.text = String.format(getString(R.string.search_radius_display), initialRadius.toInt())

        binding.radiusSlider.addOnChangeListener { slider, value, fromUser ->
            textview.text = String.format(getString(R.string.search_radius_display), value.toInt())
            (activity as SettingsActivity).selectedRadius = value.toInt()
        }
    }

    override fun onItemClickListener(categoryTag: String) {
        (activity as SettingsActivity).onCategoryClicked(categoryTag)
    }

    override fun onItemClickListener(result: SavedResult, option: String) {
        TODO("Not yet implemented")
    }

    private fun fillCategoriesSettings(){
        val categoriesData = CategoriesData()
        categoriesSettings = arrayListOf()
        for(i in 0 until categoriesData.allCategories.size){
            var categoryChecked = false
            if((activity as SettingsActivity).selectedCategories.contains(categoriesData.allCategories[i]))
                categoryChecked = true

            val categorySetting = CategorySetting(categoriesData.allCategoriesNames[i],
                categoriesData.allCategories[i], categoriesData.allCategoriesIcons[i], categoryChecked)
            categoriesSettings.add(categorySetting)
        }
    }
}