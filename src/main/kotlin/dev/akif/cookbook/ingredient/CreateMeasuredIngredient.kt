package dev.akif.cookbook.ingredient

data class CreateMeasuredIngredient(
    val name: String,
    val vegetarian: Boolean,
    val value: Double,
    val unit: Unit
)
