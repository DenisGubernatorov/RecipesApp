package com.example.recipesapp.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe

@Database(entities = [Category::class, Recipe::class], version = 3)
@TypeConverters(ConvertersUtils::class)
abstract class RecipesDatabase : RoomDatabase() {
    companion object {
        private var INSTANCE: RecipesDatabase? = null

        fun getDatabase(context: Context): RecipesDatabase {
            return synchronized(this) {
                INSTANCE?.let {
                    Log.d("RRD", "get existing DB(${it.hashCode()})")
                    it
                } ?: Room.databaseBuilder(
                    context.applicationContext,
                    RecipesDatabase::class.java,
                    "recipes_database"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE = it
                    Log.d("RRD", "create DB(${it.hashCode()})")
                }
            }
        }
    }

    abstract fun categoriesDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao

}