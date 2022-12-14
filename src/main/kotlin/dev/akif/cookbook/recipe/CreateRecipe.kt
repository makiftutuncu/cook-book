package dev.akif.cookbook.recipe

import dev.akif.cookbook.ingredient.CreateMeasuredIngredient
import dev.akif.cookbook.ingredient.Ingredient
import dev.akif.cookbook.ingredient.MeasuredIngredient

data class CreateRecipe(
    val name: String,
    val numberOfServings: Int,
    val ingredients: Map<String, List<CreateMeasuredIngredient>>,
    val instructions: List<String>
) {
    fun toLabelledMeasuredIngredients(): Map<String, List<MeasuredIngredient>> =
        ingredients
            .mapValues { list ->
                list
                    .value
                    .map { MeasuredIngredient(Ingredient(id = -1, name = it.name, vegetarian = it.vegetarian), it.value, it.unit) }
            }
}
