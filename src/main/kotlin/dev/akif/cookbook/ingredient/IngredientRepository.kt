package dev.akif.cookbook.ingredient

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IngredientRepository: CoroutineCrudRepository<IngredientEntity, Long> {
    fun findAllByIdIn(ids: Set<Long>): Flow<IngredientEntity>

    fun findAllByNameIn(names: Set<String>): Flow<IngredientEntity>
}
