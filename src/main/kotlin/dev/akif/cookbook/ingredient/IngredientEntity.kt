package dev.akif.cookbook.ingredient

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "ingredients")
class IngredientEntity(
    @Id
    var id: Long? = null,
    var name: String = "",
    var vegetarian: Boolean = false
) {
    fun toIngredient(): Ingredient =
        Ingredient(
            id = id ?: -1,
            name = name,
            vegetarian = vegetarian
        )
}
