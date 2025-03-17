package com.example.recipesapp.data

import android.util.Log
import androidx.room.TypeConverter
import com.example.recipesapp.model.Ingredient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ConvertersUtils {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromIngredientList(value: List<Ingredient>): String {
        return try {
            json.encodeToString(value)
        } catch (e: Exception) {
            Log.e("Converters", "Failed to serialize ingredients", e)
            "[]"
        }
    }

    @TypeConverter
    fun toIngredientList(value: String): List<Ingredient> {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            Log.e("Converters", "Failed to deserialize ingredients", e)
            emptyList()
        }
    }

    @TypeConverter
    fun fromMethodList(value: List<String>): String {
        return try {
            json.encodeToString(value)
        } catch (e: Exception) {
            Log.e("Converters", "Failed to serialize method", e)
            "[]"
        }
    }

    @TypeConverter
    fun toMethodList(value: String): List<String> {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            Log.e("Converters", "Failed to deserialize method", e)
            emptyList()
        }
    }
}
