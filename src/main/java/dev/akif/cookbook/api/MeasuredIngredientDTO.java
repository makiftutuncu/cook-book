package dev.akif.cookbook.api;

import dev.akif.cookbook.ingredient.CreateMeasuredIngredient;
import dev.akif.cookbook.ingredient.Unit;

public record MeasuredIngredientDTO(
        String name,
        boolean vegetarian,
        double value,
        Unit unit
) {
    public CreateMeasuredIngredient toCreateMeasuredIngredient() {
        return new CreateMeasuredIngredient(name, vegetarian, value, unit);
    }
}
