package com.example.myapplication.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.navigation.ui.AppBarConfiguration
import com.example.myapplication.data.Categories
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var toolbar: Toolbar

    var selectedCategories = arrayListOf(
        Categories.ATM, Categories.BANK, Categories.RESTAURANT,
        Categories.BAR, Categories.CAFE, Categories.MOVIE_THEATER, Categories.GYM, Categories.LIBRARY, Categories.PARK,
        Categories.POST_OFFICE, Categories.CHURCH, Categories.DOCTOR, Categories.DRUGSTORE, Categories.PHARMACY,
        Categories.HOSPITAL, Categories.PRIMARY_SCHOOL, Categories.SECONDARY_SCHOOL, Categories.UNIVERSITY,
        Categories.SHOPPING_MALL, Categories.CONVENIENCE_STORE, Categories.SUPERMARKET,
        Categories.BUS_STATION, Categories.TRAIN_STATION)
    var selectedRadius = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedCategories = intent.getStringArrayListExtra("SELECTED_CATEGORIES") as ArrayList<String>
        selectedRadius = intent.getIntExtra("SELECTED_RADIUS", 100)

        // Set toolbar
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Settings"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent()
            intent.putStringArrayListExtra("SELECTED_CATEGORIES", selectedCategories)
            intent.putExtra("SELECTED_RADIUS", selectedRadius)
            setResult(RESULT_OK, intent)
            finish()
        }

        val settingsFragment = SettingsFragment()
        val bundle = Bundle()
        bundle.putStringArrayList("SELECTED_CATEGORIES", selectedCategories)
        settingsFragment.arguments = bundle
        supportFragmentManager.commit {
            add(R.id.settinsFragment, settingsFragment, null)
        }

    }

    fun onCategoryClicked(tag: String){
        if(selectedCategories.contains(tag))
            selectedCategories.remove(tag)
        else
            selectedCategories.add(tag)
    }

}