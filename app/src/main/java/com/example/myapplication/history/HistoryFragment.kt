package com.example.myapplication.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.OnItemClickListener
import com.example.myapplication.R
import com.example.myapplication.data.Tags
import com.example.myapplication.data.database.ResultViewModel
import com.example.myapplication.data.database.SavedResult
import com.example.myapplication.databinding.FragmentHistoryBinding
import com.example.myapplication.databinding.FragmentSettingsBinding
import com.example.myapplication.results.LoadingFragment
import com.example.myapplication.settings.SettingsAdapter

class HistoryFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var resultVm: ResultViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.adapter = HistoryAdapter(this)
        //this.adapter.setData(categoriesSettings)
        resultVm = ViewModelProvider(this).get(ResultViewModel::class.java)
        resultVm.readResults.observe(viewLifecycleOwner, Observer {result ->
            adapter.setData(result as ArrayList<SavedResult>)
        })
        Log.d(Tags.HISTORY, resultVm.readResults.value.toString())
        binding.historyRv.layoutManager = LinearLayoutManager(context)
        binding.historyRv.adapter = this.adapter
    }

    override fun onItemClickListener(categoryTag: String) {
        TODO("Not yet implemented")
    }

    override fun onItemClickListener(result: SavedResult, option: String) {
        if(option == "CHECK"){
            val resultFragment = LoadingFragment()
            val bundle = Bundle()
            bundle.putParcelable("RESULT", result)
            resultFragment.arguments = bundle
            parentFragmentManager.commit{
                replace(R.id.fragment, resultFragment)
                addToBackStack("nom")

                (activity as HistoryActivity).selectedResults = arrayListOf()
                (activity as HistoryActivity).compareAvailable = false
            }
        } else if(option == "COMPARE") {
            if((activity as HistoryActivity).selectedResults.contains(result)) {
                (activity as HistoryActivity).selectedResults.remove(result)
            } else {
                (activity as HistoryActivity).selectedResults.add(result)
            }
            (activity as HistoryActivity).compareAvailable = (activity as HistoryActivity).selectedResults.size == 2
            Log.d(Tags.HISTORY, (activity as HistoryActivity).selectedResults.size.toString())
        }
    }
}