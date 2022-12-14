package dev.akif.cookbook.api;

import dev.akif.cookbook.recipe.Recipe;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record RecipeDTO(
        long id,
        String name,
        int numberOfServings,
        Map<String, List<MeasuredIngredientDTO>> ingredients,
        List<String> instructions
) {
    public static RecipeDTO from(Recipe recipe) {
        return new RecipeDTO(
                recipe.id(),
                recipe.name(),
                recipe.numberOfServings(),
                recipe
                        .ingredients()
                        .entrySet()
                        .stream()
                        .map(e -> Map.entry(e.getKey(), e.getValue().stream().map(MeasuredIngredientDTO::from).toList()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                recipe.instructions()
        );
    }
}
