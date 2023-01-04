package dev.akif.cookbook.recipe

import dev.akif.cookbook.ingredient.Ingredient
import dev.akif.cookbook.ingredient.MeasuredIngredient
import dev.akif.cookbook.ingredient.UpdateMeasuredIngredient

data class UpdateRecipe(
    val name: String,
    val numberOfServings: Int,
    val ingredients: Map<String, List<UpdateMeasuredIngredient>>,
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
