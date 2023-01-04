package dev.akif.cookbook.api

import dev.akif.cookbook.recipe.UpdateRecipe

data class UpdateRecipeDTO(
    val name: String,
    val numberOfServings: Int,
    val ingredients: Map<String, List<MeasuredIngredientDTO>>,
    val instructions: List<String>
) {
    fun toUpdateRecipe(): UpdateRecipe =
        UpdateRecipe(
            name = name,
            numberOfServings = numberOfServings,
            ingredients = ingredients.mapValues { (_, cmi) -> cmi.map { it.toUpdateMeasuredIngredient() } },
            instructions = instructions
        )
}
