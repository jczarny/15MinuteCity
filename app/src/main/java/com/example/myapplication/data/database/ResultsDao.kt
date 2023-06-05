package com.example.myapplication.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.*

@Dao
interface ResultsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(result: SavedResult)

    @Delete
    suspend fun delete(result: SavedResult)

    @Update
    suspend fun update(result: SavedResult)

    @Query("select * from results_table")
    fun selectAll() : LiveData<List<SavedResult>>

    @Query("delete from results_table")
    suspend fun dropDb()
}