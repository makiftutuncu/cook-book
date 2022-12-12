package dev.akif.cookbook.ingredient;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IngredientRepository extends CrudRepository<IngredientEntity, Long> {
    List<IngredientEntity> findAllByIdIn(Set<Long> ids);
}
