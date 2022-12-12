package dev.akif.cookbook.recipe;

import dev.akif.cookbook.ingredient.MeasuredIngredient;

import java.util.List;
import java.util.Map;

public record Recipe(
        long id,
        String name,
        int numberOfServings,
        Map<String, List<MeasuredIngredient>> ingredients,
        List<String> instructions
) {
}
