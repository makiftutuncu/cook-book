package dev.akif.cookbook.recipe.ingredient

import dev.akif.cookbook.ingredient.Unit
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "recipe_ingredients")
class RecipeIngredientEntity(
    @Id
    var id: Long? = null,
    var recipeId: Long = -1,
    var ingredientId: Long = -1,
    var label: String = "",
    var value: Double = 0.0,
    var unit: Unit = Unit.QUANTITY,
    var sortOrder: Int = 0
)
