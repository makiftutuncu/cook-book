package dev.akif.cookbook.recipe.ingredient

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipeIngredientRepository: CoroutineCrudRepository<RecipeIngredientEntity, Long> {
    @Query(
        """SELECT ri.id, ri.recipe_id, ri.ingredient_id, ri.label, ri.value, ri.unit, ri.sort_order
           FROM recipe_ingredients ri JOIN recipes r ON ri.recipe_id = r.id JOIN ingredients i ON ri.ingredient_id = i.id
           WHERE r.id = :recipeId
           ORDER BY ri.label, ri.sort_order"""
    )
    fun findByRecipeId(recipeId: Long): Flow<RecipeIngredientEntity>

    fun deleteAllByRecipeId(recipeId: Long): Flow<Unit>
}
