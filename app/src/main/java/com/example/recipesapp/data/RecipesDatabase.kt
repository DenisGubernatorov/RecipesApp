package com.example.recipesapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recipesapp.model.Category

@Database(entities = [Category::class], version = 1)
abstract class RecipesDatabase : RoomDatabase() {
    companion object {
        private var INSTANCE: RecipesDatabase? = null

        fun getDatabase(context: Context): RecipesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RecipesDatabase::class.java,
                    "recipes_database"
                ).build().also { INSTANCE = it }
            }
        }
    }

    abstract fun categoriesDao(): CategoriesDao
}