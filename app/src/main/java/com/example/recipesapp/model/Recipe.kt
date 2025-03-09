package com.example.recipesapp.model

import kotlinx.serialization.Serializable


@Serializable
data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String?
)