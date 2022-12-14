package dev.akif.cookbook.ingredient;

public record CreateMeasuredIngredient(
        String name,
        boolean vegetarian,
        double value,
        Unit unit
) {
}
