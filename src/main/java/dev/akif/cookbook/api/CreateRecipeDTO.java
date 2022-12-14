package dev.akif.cookbook.api;

import java.util.List;
import java.util.Map;

public record CreateRecipeDTO(
        String name,
        int numberOfServings,
        Map<String, List<MeasuredIngredientDTO>> ingredients,
        List<String> instructions
) {
}
