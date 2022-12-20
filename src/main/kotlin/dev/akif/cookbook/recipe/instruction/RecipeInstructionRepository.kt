package dev.akif.cookbook.recipe.instruction

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipeInstructionRepository: CoroutineCrudRepository<RecipeInstructionEntity, Long> {
    @Query(
        """SELECT ri.id, ri.recipe_id, ri.sort_order, ri.instruction
           FROM recipe_instructions ri JOIN recipes r ON ri.recipe_id = r.id
           WHERE r.id = :recipeId
           ORDER BY ri.sort_order, ri.instruction"""
    )
    fun findByRecipeId(recipeId: Long): Flow<RecipeInstructionEntity>
}
