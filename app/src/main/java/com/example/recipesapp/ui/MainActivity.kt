package com.example.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.model.Category
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.i("thread", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        val thread = Thread {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.connect()
                Log.i("thread", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

                val bufferedData =
                    connection.inputStream.bufferedReader().use { reader -> reader.readText() }
                Log.i("data", bufferedData)

                val json = Json { ignoreUnknownKeys = true }
                val categories: List<Category> = json.decodeFromString(bufferedData)
                categories.forEach { category ->
                    Log.i(
                        "category",
                        "ID: ${category.id}, Name: ${category.title}, description: ${category.description}, imageURL: ${category.imageUrl}"
                    )
                }
            } catch (e: Exception) {
                Log.e("connection", e.toString())
            } finally {
                connection.disconnect()
            }
        }

        thread.start()

        binding.category.setOnClickListener {
            val navController = findNavController(R.id.mainContainer)
            if (navController.currentDestination?.id != R.id.categoriesListFragment) {
                navController.navigate(R.id.global_action_for_categories_list)
            }
        }

        binding.favorites.setOnClickListener {
            val navController = findNavController(R.id.mainContainer)
            if (navController.currentDestination?.id != R.id.favoritesFragment) {
                navController.navigate(R.id.global_action_for_favorites)
            }
        }
    }

}