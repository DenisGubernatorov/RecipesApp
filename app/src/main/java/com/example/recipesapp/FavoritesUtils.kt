package com.example.recipesapp

import android.content.Context


class FavoritesUtils {
    private val favoriteFileKey = "favorites"
    private val idsKey = "ids"

    fun getFavorites(context: Context?): HashSet<String> {

        val sharedPrefs = context?.getSharedPreferences(favoriteFileKey, Context.MODE_PRIVATE)
        return sharedPrefs?.getStringSet(idsKey, HashSet<String>())?.toHashSet() ?: HashSet()
    }

    fun setFavorites(context: Context?, ids: MutableSet<String>?) {
        val sharedPrefs =
            context?.getSharedPreferences(favoriteFileKey, Context.MODE_PRIVATE) ?: return
        sharedPrefs.edit().putStringSet(idsKey, ids).apply()

    }
}