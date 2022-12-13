package dev.akif.cookbook.recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends PagingAndSortingRepository<RecipeEntity, Long>, JpaSpecificationExecutor<RecipeEntity> {
    @Query("""
           SELECT r
           FROM recipe r
           WHERE (:name IS NULL OR LOWER(r.name) LIKE CONCAT('%', LOWER(:name), '%')) AND (:numberOfServings IS NULL OR r.numberOfServings >= :numberOfServings)
           ORDER BY r.name, r.numberOfServings
           """)
    Page<RecipeEntity> findAllBy(String name, Integer numberOfServings, Pageable pageable);
}
