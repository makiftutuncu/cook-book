package dev.akif.cookbook.recipe;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Entity(name = "recipe_instruction")
@Getter
@IdClass(RecipeInstructionEntity.RecipeInstructionId.class)
@NoArgsConstructor
@Setter
@Table(name = "recipe_instructions")
@ToString
public class RecipeInstructionEntity {
    @Id
    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;

    @Id
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "instruction", nullable = false)
    private String instruction;

    @Data
    @NoArgsConstructor
    public static class RecipeInstructionId implements Serializable {
        private Long recipeId;
        private Integer sortOrder;
    }
}
