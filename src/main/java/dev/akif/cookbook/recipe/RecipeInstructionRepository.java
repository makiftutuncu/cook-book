package dev.akif.cookbook.recipe;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeInstructionRepository extends ListCrudRepository<RecipeInstructionEntity, RecipeInstructionEntity.RecipeInstructionId> {
    @Query("""
           SELECT ri
           FROM recipe_instruction ri
           JOIN recipe r ON ri.recipeId = r.id
           WHERE r.id = :recipeId
           ORDER BY ri.sortOrder, ri.instruction
           """)
    List<RecipeInstructionEntity> findByRecipeId(Long recipeId);
}
