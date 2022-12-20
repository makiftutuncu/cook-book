package dev.akif.cookbook.recipe

import dev.akif.cookbook.ingredient.CreateMeasuredIngredient

data class CreateRecipe(
    val name: String,
    val numberOfServings: Int,
    val ingredients: Map<String, List<CreateMeasuredIngredient>>,
    val instructions: List<String>
)
