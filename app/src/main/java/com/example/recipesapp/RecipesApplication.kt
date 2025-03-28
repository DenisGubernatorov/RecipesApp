package com.example.recipesapp

import android.app.Application
import com.example.recipesapp.di.AppContainer

class RecipesApplication : Application() {
    val appContainer by lazy { AppContainer(this) }

}