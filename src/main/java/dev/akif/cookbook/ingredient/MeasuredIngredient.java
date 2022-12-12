package dev.akif.cookbook.ingredient;

public record MeasuredIngredient(
        Ingredient ingredient,
        double value,
        Unit unit
) {
}
