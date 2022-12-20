package dev.akif.cookbook.api

import dev.akif.cookbook.recipe.Recipe

data class RecipeDTO(
    val id: Long,
    val name: String,
    val numberOfServings: Int,
    val ingredients: Map<String, List<MeasuredIngredientDTO>>,
    val instructions: List<String>
) {
    companion object {
        fun from(recipe: Recipe): RecipeDTO =
            RecipeDTO(
                id = recipe.id,
                name = recipe.name,
                numberOfServings = recipe.numberOfServings,
                ingredients = recipe.ingredients.mapValues { (_, mi) -> mi.map(MeasuredIngredientDTO::from) },
                instructions = recipe.instructions,
            )
    }
}
