package com.example.myapplication.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import com.example.myapplication.R
import com.example.myapplication.data.Tags
import com.example.myapplication.data.database.SavedResult

class HistoryActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar

    var selectedResults = arrayListOf<SavedResult>()
    var compareAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Search History"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val historyFragment = HistoryFragment()
        supportFragmentManager.commit{
            add(R.id.fragment, historyFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.compare_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.compare){
            Log.d(Tags.HISTORY, selectedResults.size.toString())
            if(compareAvailable){
                val compareFragment = CompareFragment()
                val bundle = Bundle()
                bundle.putParcelableArrayList("RESULTS", selectedResults)
                compareFragment.arguments = bundle
                supportFragmentManager.commit {
                    replace(R.id.fragment, compareFragment)
                    addToBackStack("compare")
                }
                selectedResults = arrayListOf()
                compareAvailable = false
            } else{
                Toast.makeText(this, "Select 2 localizations", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}