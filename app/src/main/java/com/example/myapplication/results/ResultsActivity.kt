package com.example.myapplication.results

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.navigation.ui.AppBarConfiguration
import com.example.myapplication.R
import com.example.myapplication.SearchNearbyAPI
import com.example.myapplication.data.Request
import com.example.myapplication.databinding.ActivityResultsBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ResultsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityResultsBinding
    lateinit var saveFab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Results"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // SHARE
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        saveFab = findViewById(R.id.fabSave)
        // SAVE
        binding.fabSave.setOnClickListener { view ->
            val fragment = supportFragmentManager.findFragmentById(R.id.fragment_result) as LoadingFragment
            fragment.saveResult()
            Snackbar.make(view, "Saved results", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
        }

        val request = intent.getParcelableExtra("REQUEST", Request::class.java)
        val bundle = Bundle()
        bundle.putParcelable("REQUEST", request)
        val loadingFragment = LoadingFragment()
        loadingFragment.arguments = bundle
        supportFragmentManager.commit {
            add(R.id.fragment_result, loadingFragment, null)
        }
    }
}