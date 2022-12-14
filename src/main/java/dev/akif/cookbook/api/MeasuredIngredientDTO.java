package dev.akif.cookbook.api;

import dev.akif.cookbook.ingredient.CreateMeasuredIngredient;
import dev.akif.cookbook.ingredient.MeasuredIngredient;
import dev.akif.cookbook.ingredient.Unit;

public record MeasuredIngredientDTO(
        String name,
        boolean vegetarian,
        double value,
        Unit unit
) {
    public static MeasuredIngredientDTO from(MeasuredIngredient measuredIngredient) {
        return new MeasuredIngredientDTO(
                measuredIngredient.ingredient().name(),
                measuredIngredient.ingredient().vegetarian(),
                measuredIngredient.value(),
                measuredIngredient.unit()
        );
    }

    public CreateMeasuredIngredient toCreateMeasuredIngredient() {
        return new CreateMeasuredIngredient(name, vegetarian, value, unit);
    }
}
