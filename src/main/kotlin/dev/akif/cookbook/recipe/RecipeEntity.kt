package dev.akif.cookbook.recipe

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "recipes")
class RecipeEntity(
    @Id
    var id: Long? = null,
    var name: String = "",
    var numberOfServings: Int = 0
)
