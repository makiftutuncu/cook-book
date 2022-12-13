package dev.akif.cookbook.ingredient;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Entity(name = "ingredient")
@Getter
@NoArgsConstructor
@Setter
@Table(name = "ingredients")
@ToString
public class IngredientEntity {
    @Column(name = "id", nullable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "vegetarian", nullable = false)
    private Boolean vegetarian;

    public Ingredient toIngredient() {
        return new Ingredient(id, name, vegetarian);
    }
}
