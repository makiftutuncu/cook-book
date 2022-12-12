package dev.akif.cookbook.recipe;

import dev.akif.cookbook.ingredient.Unit;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@Entity(name = "recipe_ingredient")
@IdClass(RecipeIngredientEntity.RecipeIngredientId.class)
@NoArgsConstructor
@Table(name = "recipe_ingredients")
public class RecipeIngredientEntity {
    @Id
    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;

    @Id
    @Column(name = "ingredient_id", nullable = false, insertable = false, updatable = false)
    private Long ingredientId;

    @Id
    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "unit", nullable = false)
    @Enumerated(EnumType.STRING)
    private Unit unit;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Data
    @NoArgsConstructor
    public static class RecipeIngredientId implements Serializable {
        private Long recipeId;
        private Long ingredientId;
        private String label;
    }
}
