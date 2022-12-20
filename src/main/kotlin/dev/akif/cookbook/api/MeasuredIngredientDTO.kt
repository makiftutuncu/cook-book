package dev.akif.cookbook.api

import dev.akif.cookbook.ingredient.CreateMeasuredIngredient
import dev.akif.cookbook.ingredient.MeasuredIngredient
import dev.akif.cookbook.ingredient.Unit

data class MeasuredIngredientDTO(
    val name: String,
    val vegetarian: Boolean,
    val value: Double,
    val unit: Unit
) {
    fun toCreateMeasuredIngredient(): CreateMeasuredIngredient =
        CreateMeasuredIngredient(
            name = name,
            vegetarian = vegetarian,
            value = value,
            unit = unit
        )

    companion object {
        fun from(measuredIngredient: MeasuredIngredient): MeasuredIngredientDTO =
            MeasuredIngredientDTO(
                name = measuredIngredient.ingredient.name,
                vegetarian = measuredIngredient.ingredient.vegetarian,
                value = measuredIngredient.value,
                unit = measuredIngredient.unit
            )
    }
}
