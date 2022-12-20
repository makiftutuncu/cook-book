package dev.akif.cookbook.recipe.instruction

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "recipe_instructions")
class RecipeInstructionEntity(
    @Id
    var id: Long? = null,
    var recipeId: Long = -1,
    var sortOrder: Int = 0,
    var instruction: String = ""
)
