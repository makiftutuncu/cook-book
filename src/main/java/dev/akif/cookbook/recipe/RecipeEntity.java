package dev.akif.cookbook.recipe;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Entity(name = "recipe")
@Getter
@NoArgsConstructor
@Setter
@Table(name = "recipes")
@ToString
public class RecipeEntity {
    @Column(name = "id", nullable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "number_of_servings", nullable = false)
    private Integer numberOfServings;
}
