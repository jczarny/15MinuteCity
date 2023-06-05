package com.example.myapplication.data.database

import androidx.lifecycle.LiveData

class Repository (private val resultDao: ResultsDao){

    val readAll: LiveData<List<SavedResult>> = resultDao.selectAll()

    suspend fun addResult(result: SavedResult){
        resultDao.insert(result)
    }
    suspend fun dropDb(){
        resultDao.dropDb()
    }
    suspend fun editResult(result: SavedResult){
        resultDao.update(result)
    }
    suspend fun deleteResult(result: SavedResult){
        resultDao.delete(result)
    }
}