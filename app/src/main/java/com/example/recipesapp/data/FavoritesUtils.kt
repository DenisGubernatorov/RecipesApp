package com.example.recipesapp.data

import android.app.Application
import android.content.Context

class FavoritesUtils(private val application: Application) {
    private val favoriteFileKey = "favorites"
    private val idsKey = "ids"

    fun getFavorites(): HashSet<Int> {

        val sharedPrefs = application.getSharedPreferences(
            favoriteFileKey,
            Context.MODE_PRIVATE
        )
        val idsAsStrings = sharedPrefs?.getStringSet(idsKey, emptySet()) ?: emptySet()
        return idsAsStrings.mapNotNull { it.toIntOrNull() }.toHashSet()

    }

    fun setFavorites(ids: Set<String>) {
        val sharedPrefs =
            application.getSharedPreferences(favoriteFileKey, Context.MODE_PRIVATE) ?: return
        sharedPrefs.edit().putStringSet(idsKey, ids).apply()

    }
}