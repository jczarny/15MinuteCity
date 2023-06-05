package com.example.myapplication.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.CategoriesData
import com.example.myapplication.data.Tags
import com.example.myapplication.data.database.SavedResult
import com.example.myapplication.databinding.FragmentCompareBinding

class CompareFragment : Fragment() {

    private var _binding: FragmentCompareBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CompareAdapter
    private var compareobjects = arrayListOf<CompareObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompareBinding.inflate(inflater, container, false)

        val results = arguments?.getParcelableArrayList("RESULTS", SavedResult::class.java) as ArrayList<SavedResult>
        compareobjects = compareByCategory(results)

        this.adapter = CompareAdapter(compareobjects)
        binding.categoryRv.layoutManager = LinearLayoutManager(context)
        binding.categoryRv.adapter = this.adapter

        var address1 = results[0].address
        if(address1.length > 18){
            address1 = address1.take(15).plus("...")
        }
        var address2 = results[1].address
        if(address2.length > 18){
            address2 = address2.take(15).plus("...")
        }
        binding.address1.text = address1
        binding.address2.text = address2
        val text = "Radius: "
        binding.radius1.text = text.plus(results[0].radius.toString()).plus(" m.")
        binding.radius2.text = text.plus(results[1].radius.toString()).plus(" m.")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun compareByCategory(results: ArrayList<SavedResult>): ArrayList<CompareObject>{
        val objects = arrayListOf<CompareObject>()

        val result1 = results[0]
        val result2 = results[1]

        val categoriesData = CategoriesData()
        val categoriesString1 = result1.string.split(';')
        val categoriesString2 = result2.string.split(';')

        for (i in 0 until categoriesString1.size-1){
            val currentItem1 = categoriesString1[i]
            val currentItem2 = categoriesString2[i]

            val places1 = currentItem1.split('_')
            val places2 = currentItem2.split('_')
            var places1size = places1.size - 1
            var places2size = places2.size - 1
            if(places1[0] == "None"){
                places1size = 0
            } else if (places1[0] == "Null"){
                places1size = -1
            }
            if(places2[0] == "None"){
                places2size = 0
            } else if (places2[0] == "Null"){
                places2size = -1
            }

            if(places1[0] == "Null" && places2[0] == "Null") {
                continue
            } else{
                val compareObject = CompareObject(categoriesData.allCategoriesNames[i], places1size, result1.radius, places2size, result2.radius, categoriesData.allCategoriesRequiredAmounts[i])
                objects.add(compareObject)
            }
        }
        return objects
    }

}