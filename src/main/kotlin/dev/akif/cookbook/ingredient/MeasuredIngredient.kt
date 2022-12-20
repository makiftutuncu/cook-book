package dev.akif.cookbook.ingredient

data class MeasuredIngredient(
    val ingredient: Ingredient,
    val value: Double,
    val unit: Unit
)
