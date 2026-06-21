package com.example.newsfeed.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsfeed.data.dao.SavedArticleDao
import com.example.newsfeed.data.entity.SavedArticle

@Database(entities = [SavedArticle::class],version =1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun savedArticleDao() : SavedArticleDao

    companion object{
        @Volatile
        private var INSTANCE : AppDatabase ?=null;

        fun getDatabase(context: Context) : AppDatabase{
            return INSTANCE ?: synchronized(this){
                val intance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,"roomdbNF.db"
                ).build()
                INSTANCE = intance
                intance
            }
        }
    }
}