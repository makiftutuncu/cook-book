package dev.akif.cookbook.recipe

import dev.akif.cookbook.ingredient.MeasuredIngredient

data class Recipe(
    val id: Long,
    val name: String,
    val numberOfServings: Int,
    val ingredients: Map<String, List<MeasuredIngredient>>,
    val instructions: List<String>
)
