package dev.akif.cookbook.recipe

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository: CoroutineCrudRepository<RecipeEntity, Long> {
    @Query(
        """SELECT r.id, r.name, r.number_of_servings
           FROM recipes r
           WHERE (:name IS NULL OR LOWER(r.name) LIKE CONCAT('%', LOWER(:name), '%')) AND (:numberOfServings IS NULL OR r.number_of_servings >= :numberOfServings)
           ORDER BY r.name, r.number_of_servings"""
    )
    fun findAllBy(name: String?, numberOfServings: Int?): Flow<RecipeEntity>
}
