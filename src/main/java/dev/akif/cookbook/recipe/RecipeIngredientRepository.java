package dev.akif.cookbook.recipe;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientRepository extends CrudRepository<RecipeIngredientEntity, RecipeIngredientEntity.RecipeIngredientId> {
    @Query("""
           SELECT ri
           FROM recipe_ingredient ri
           JOIN recipe r ON ri.recipeId = r.id
           JOIN ingredient i ON ri.ingredientId = i.id
           WHERE r.id = :recipeId
           ORDER BY ri.label, ri.sortOrder
           """)
    List<RecipeIngredientEntity> findByRecipeId(Long recipeId);
}
