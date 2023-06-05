package com.example.myapplication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedResult::class], version = 1)
abstract class ResultsDb : RoomDatabase(){
    abstract fun resultsDao(): ResultsDao

    companion object ResultDb{
        @Volatile
        private var db: ResultsDb? = null

        fun getInstance(context: Context): ResultsDb{
            val tempDb = db
            if(tempDb != null){
                return tempDb
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, ResultsDb::class.java, "results-db").build()
                db = instance
                return instance
            }
        }
    }
}