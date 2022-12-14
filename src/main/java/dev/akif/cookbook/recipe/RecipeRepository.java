package dev.akif.cookbook.recipe;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends PagingAndSortingRepository<RecipeEntity, Long>, ListCrudRepository<RecipeEntity, Long> {
    @Query("""
           SELECT r
           FROM recipe r
           WHERE (:name IS NULL OR LOWER(r.name) LIKE CONCAT("%", LOWER(:name), "%")) AND (:numberOfServings IS NULL OR r.numberOfServings >= :numberOfServings)
           ORDER BY r.name, r.numberOfServings
           """)
    List<RecipeEntity> findAllBy(String name, Integer numberOfServings);
}
