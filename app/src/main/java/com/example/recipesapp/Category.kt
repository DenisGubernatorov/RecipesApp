package com.example.recipesapp

import java.net.URL

data class Category(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: URL
)