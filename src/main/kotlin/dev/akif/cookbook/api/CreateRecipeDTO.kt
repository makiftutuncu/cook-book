package dev.akif.cookbook.api

import dev.akif.cookbook.recipe.CreateRecipe

data class CreateRecipeDTO(
    val name: String,
    val numberOfServings: Int,
    val ingredients: Map<String, List<MeasuredIngredientDTO>>,
    val instructions: List<String>
) {
    fun toCreateRecipe(): CreateRecipe =
        CreateRecipe(
            name = name,
            numberOfServings = numberOfServings,
            ingredients = ingredients.mapValues { (_, cmi) -> cmi.map { it.toCreateMeasuredIngredient() } },
            instructions = instructions
        )
}
