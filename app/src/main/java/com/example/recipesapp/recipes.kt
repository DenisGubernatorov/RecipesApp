package com.example.recipesapp


object STUB {

    private val categories = listOf(
        Category(0, "Бургеры", "Рецепты всех популярных видов бургеров", "cat_burger.png"),
        Category(
            1,
            "Десерты",
            "Самые вкусные рецепты десертов специально для вас",
            "cat_dessert.png"
        ),
        Category(
            2,
            "Пицца",
            "Пицца на любой вкус и цвет. Лучшая подборка для тебя",
            "cat_pizza.png"
        ),
        Category(3, "Рыба", "Печеная, жареная, сушеная, любая рыба на твой вкус", "cat_fish.png"),
        Category(4, "Супы", "От классики до экзотики: мир в одной тарелке", "cat_soup.png"),
        Category(5, "Салаты", "Хрустящий калейдоскоп под соусом вдохновения", "cat_salad.png")
    )

    fun getCategories(): List<Category> = categories

}

