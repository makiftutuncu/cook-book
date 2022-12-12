package dev.akif.cookbook.api;

import dev.akif.cookbook.ingredient.MeasuredIngredient;
import dev.akif.cookbook.recipe.Recipe;

import java.util.List;
import java.util.Map;

public record RecipeDTO(
        long id,
        String name,
        int numberOfServings,
        Map<String, List<MeasuredIngredient>> ingredients,
        List<String> instructions
) {
    public static RecipeDTO from(Recipe recipe) {
        return new RecipeDTO(recipe.id(), recipe.name(), recipe.numberOfServings(), recipe.ingredients(), recipe.instructions());
    }
}
