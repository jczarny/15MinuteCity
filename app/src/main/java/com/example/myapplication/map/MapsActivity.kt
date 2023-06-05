package com.example.myapplication.map

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import com.example.myapplication.R
import com.example.myapplication.data.CategoriesData
import com.example.myapplication.data.Tags
import com.example.myapplication.databinding.ActivityMapsBinding
import com.example.myapplication.history.HistoryActivity
import com.example.myapplication.settings.SettingsActivity


class MapsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var binding: ActivityMapsBinding
    var selectedCategories = CategoriesData().allCategories
    var selectedRadius = 100

    val startForResult = registerForActivityResult(StartActivityForResult()) {result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedCategories = result.data!!.getStringArrayListExtra("SELECTED_CATEGORIES") as ArrayList<String>
            selectedRadius = result.data!!.getIntExtra("SELECTED_RADIUS", 100)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "City15"
        setSupportActionBar(toolbar)

        val mapFragment = MapFragment()
        supportFragmentManager.commit {
            add(R.id.mapfragment, mapFragment, null)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settings){
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putStringArrayListExtra("SELECTED_CATEGORIES", selectedCategories)
            intent.putExtra("SELECTED_RADIUS", selectedRadius)
            startForResult.launch(intent)
        } else if (item.itemId == R.id.history) {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun hideKeyboard(){
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
