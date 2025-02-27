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
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val mainUrl = " https://recipes.androidsprint.ru/api"
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val threadPool = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.i("thread", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        val thread = Thread {
            val url = URL("$mainUrl/category")
            val connForCategory = url.openConnection() as HttpURLConnection
            try {
                connForCategory.connect()
                Log.i("thread", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

                val bufferedData =
                    connForCategory.inputStream.bufferedReader().use { reader -> reader.readText() }
                Log.i("data", bufferedData)

                val json = Json { ignoreUnknownKeys = true }
                val categories: List<Category> = json.decodeFromString(bufferedData)

                val catIDs = categories.map { it.id }
                for (id in catIDs) {
                    val getRecipesInCategoryTask = getTaskForRecipesList(url, id)
                    threadPool.submit(getRecipesInCategoryTask)
                }

            } catch (e: Exception) {
                Log.e("getCategoriesTask", e.toString())
            } finally {
                connForCategory.disconnect()
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

    private fun getTaskForRecipesList(url: URL, id: Int) = Runnable {

        val categoryUrl = URL("${url}/${id}/recipes")
        val connForRecipesList = categoryUrl.openConnection() as HttpURLConnection
        try {
            val recipes =
                connForRecipesList.inputStream.bufferedReader().use { reader -> reader.readText() }
            Log.i("recipeList", "${Thread.currentThread().name}: $recipes")
        } catch (e: Exception) {
            Log.e("getRecipesTask", e.toString())
        } finally {
            connForRecipesList.disconnect()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}