package com.example.myapplication.results

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.example.myapplication.data.*
import com.example.myapplication.data.database.ResultViewModel
import com.example.myapplication.data.database.SavedResult
import com.example.myapplication.databinding.FragmentLoadingBinding
import com.example.myapplication.databinding.ShimmerResultLayoutBinding
import com.example.myapplication.map.MapsActivity
import com.example.myapplication.results.recyclerview_outer.CategoriesAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.maps.model.LatLng
import kotlin.concurrent.thread
import kotlin.math.max
import kotlin.math.min


class LoadingFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentLoadingBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CategoriesAdapter
    private lateinit var address: String
    private var radius: Int = 0

    private lateinit var shimmerlayout: ShimmerFrameLayout
    private var categoriesList: ArrayList<CategoryItem> = arrayListOf()
    private var totalScore: Int = 0
    private var maxScore: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)

        this.adapter = CategoriesAdapter(this)
        binding.outerRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.outerRecyclerView.adapter = this.adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmerlayout = requireActivity().findViewById(R.id.shimmer)

        val request: Request? = arguments?.getParcelable("REQUEST", Request::class.java)
        val result: SavedResult? = arguments?.getParcelable("RESULT", SavedResult::class.java)
        if(request != null){
            handleRequest(request)
        } else if (result != null){
            showResult(result)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleRequest(request: Request){
        shimmerlayout.startShimmer()

        Log.d(Tags.RESULTS, request.radius.toString())
        val latLng = LatLng(request.latitude, request.longitude)
        val searchAPI = SearchNearbyAPI(
            request.categories!!,
            request.key!!,
            request.radius,
            latLng
        )
        address = searchAPI.getLocation(requireContext(),latLng)
        radius = request.radius
        binding.address.text = address
        val text = "Radius: "
        binding.radius.text = text.plus(radius.toString()).plus(" m.")

        val categoriesData = CategoriesData()

        thread{
            for (category in request.categories){
                val reqResult = searchAPI.requestSearch(category)
                val catName = categoriesData.getNameByTag(category)
                val categoryItem = CategoryItem(catName, reqResult)
                categoriesList.add(categoryItem)
            }
            val (totalScore, maxScore) = categoriesData.evaluate(categoriesList)
            this.totalScore = totalScore
            this.maxScore = maxScore

            activity?.runOnUiThread {
                binding.score.text = totalScore.toString()
                binding.resultCard.setCardBackgroundColor(
                    categoriesData.colorEvaluation(totalScore, maxScore)
                )
                this@LoadingFragment.adapter.setData(categoriesList)
                this@LoadingFragment.shimmerlayout.stopShimmer()
                this@LoadingFragment.shimmerlayout.visibility = View.GONE
                this@LoadingFragment.binding.outerRecyclerView.visibility = View.VISIBLE
                (activity as ResultsActivity).saveFab.visibility = View.VISIBLE
            }
        }
    }

    private fun showResult(result: SavedResult){
        this.shimmerlayout.visibility = View.GONE
        this.binding.outerRecyclerView.visibility = View.VISIBLE

        binding.address.text = result.address
        binding.score.text = result.rating.toString()

        val text = "Radius: "
        binding.radius.text = text.plus(result.radius.toString()).plus(" m.")

        val categoriesString = result.string.split(';')

        val categoriesData = CategoriesData()
        for (i in 0 until categoriesString.size-1){
            val currentItem = categoriesString[i]
            val places = currentItem.split('_')
            if(places[0] == "Null"){
                continue
            } else if (places[0] == "None") {
                val categoryItem = CategoryItem(categoriesData.allCategoriesNames[i], arrayListOf())
                categoriesList.add(categoryItem)
            } else {
                val a = arrayListOf<Place>()
                for(place in places){
                    if(place != ""){
                        val name_address = place.split("*")
                        try{
                            val _place = Place(name_address[0], name_address[1])
                            a.add(_place)
                        } catch(e: Exception){
                            Log.d(Tags.RESULTS, name_address.toString())
                        }
                    }
                }
                val categoryItem = CategoryItem(categoriesData.allCategoriesNames[i], a)
                categoriesList.add(categoryItem)
                this@LoadingFragment.adapter.setData(categoriesList)
            }
        }
        val (totalScore, maxScore) = categoriesData.evaluate(categoriesList)
        this.totalScore = totalScore
        this.maxScore = maxScore
        binding.resultCard.setCardBackgroundColor(
            categoriesData.colorEvaluation(totalScore, maxScore)
        )
    }

    override fun onItemClickListener(categoryTag: String) {
        Log.d(Tags.RESULTS, "EEE")
    }

    override fun onItemClickListener(result: SavedResult, option: String) {
        TODO("Not yet implemented")
    }

    fun saveResult(){
        val categoriesData = CategoriesData()
        val allCategoriesNames = categoriesData.allCategoriesNames
        var string = ""
        for (category_name in allCategoriesNames){
            var index = 0
            var found = false
            for(category_local in categoriesList) {
                if (category_local.categoryName == category_name) {
                    if(category_local.placesItems.isEmpty())
                        string = string.plus("None")
                    else{
                        for (place in category_local.placesItems){
                            val n = "_" + refactorStringToSave(place.name!!)
                            val s = "*" + refactorStringToSave(place.address!!)
                            string = string.plus(n).plus(s)
                        }
                    }
                    string = string.plus(";")
                    found = true
                    break
                } else {
                    index += 1
                }
            }
            if(!found)
                string = string.plus("Null;")
        }
        val res = SavedResult(0, totalScore, maxScore, radius, address, string)

        val resultVm = ViewModelProvider(this).get(ResultViewModel::class.java)
        resultVm.addResult(res)
        Log.d(Tags.RESULTS, resultVm.readResults.value.toString())
    }

    private fun refactorStringToSave(input: String): String{
        var output = input.replace(';', ' ')
        output = output.replace('_', ' ')
        output = output.replace('*', ' ')
        return output
    }
}