package dev.akif.cookbook.ingredient;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IngredientRepository extends ListCrudRepository<IngredientEntity, Long> {
    List<IngredientEntity> findAllByIdIn(Set<Long> ids);

    List<IngredientEntity> findAllByNameIn(Set<String> names);
}
