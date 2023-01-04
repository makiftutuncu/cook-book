package dev.akif.cookbook.ingredient

data class UpdateMeasuredIngredient(
    val name: String,
    val vegetarian: Boolean,
    val value: Double,
    val unit: Unit
)
