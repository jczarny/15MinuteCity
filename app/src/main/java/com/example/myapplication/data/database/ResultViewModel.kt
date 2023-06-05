package com.example.myapplication.data.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultViewModel(app: Application) : AndroidViewModel(app) {

    var readResults: LiveData<List<SavedResult>>
    private val repo: Repository

    init {
        val resultsDao = ResultsDb.getInstance(app).resultsDao()
        repo = Repository(resultsDao)
        readResults = repo.readAll
    }

    fun addResult(result: SavedResult){
        viewModelScope.launch(Dispatchers.IO){
            repo.addResult(result)
        }
    }

    fun dropDb(){
        viewModelScope.launch(Dispatchers.IO){
            repo.dropDb()
        }
    }

    fun editResult(result: SavedResult){
        viewModelScope.launch(Dispatchers.IO){
            repo.editResult(result)
        }
    }

    fun deleteResult(result: SavedResult){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteResult(result)
        }
    }

}