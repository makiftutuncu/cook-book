package dev.akif.cookbook

import dev.akif.cookbook.api.*
import dev.akif.cookbook.ingredient.Unit

val pancakesRecipeDTO = RecipeDTO(
    1L,
    "Pancakes",
    4,
    mapOf(
        "For Batter" to listOf(
            MeasuredIngredientDTO("Egg", true, 2.0, Unit.QUANTITY),
            MeasuredIngredientDTO("Sugar", true, 0.5, Unit.CUPS),
            MeasuredIngredientDTO("Flour", true, 2.0, Unit.CUPS),
            MeasuredIngredientDTO("Milk", true, 1.5, Unit.CUPS),
            MeasuredIngredientDTO("Oil", true, 2.0, Unit.SPOONS),
            MeasuredIngredientDTO("Baking Powder", true, 1.0, Unit.PACKS),
            MeasuredIngredientDTO("Vanilla", true, 1.0, Unit.PACKS),
            MeasuredIngredientDTO("Salt", true, 1.0, Unit.PINCHES)
        ),
        "For Cooking" to listOf(
            MeasuredIngredientDTO("Oil", true, 2.0, Unit.SPOONS)
        ),
        "For Serving" to listOf(
            MeasuredIngredientDTO("Honey", true, 3.0, Unit.SPOONS)
        )
    ),
    listOf(
        "Break eggs and add sugar.",
        "Mix until creamy.",
        "Add oil and milk and continue mixing.",
        "Add flour, baking powder, vanilla and salt.",
        "Mix until smooth.",
        "Slightly oil a pan and heat. Reduce heat once pan is hot.",
        "Pour mixture into pan.",
        "Cook until bubbles appear on top and flip to cook the other side.",
        "Cook all of the batter the same way.",
        "Pour honey on top and serve."
    )
)

val updatedPancakesRecipeDTO = RecipeDTO(
    1L,
    "Pancakes Updated",
    4,
    mapOf(
        "For Batter Updated" to listOf(
            MeasuredIngredientDTO("Egg Updated", true, 2.0, Unit.QUANTITY),
            MeasuredIngredientDTO("Sugar Updated", true, 0.5, Unit.CUPS),
            MeasuredIngredientDTO("Flour Updated", true, 2.0, Unit.CUPS),
            MeasuredIngredientDTO("Milk Updated", true, 1.5, Unit.CUPS),
            MeasuredIngredientDTO("Oil Updated", true, 2.0, Unit.SPOONS),
            MeasuredIngredientDTO("Baking Powder Updated", true, 1.0, Unit.PACKS),
            MeasuredIngredientDTO("Vanilla Updated", true, 1.0, Unit.PACKS),
            MeasuredIngredientDTO("Salt Updated", true, 1.0, Unit.PINCHES)
        ),
        "For Cooking Updated" to listOf(
            MeasuredIngredientDTO("Oil Updated", true, 2.0, Unit.SPOONS)
        ),
        "For Serving Updated" to listOf(
            MeasuredIngredientDTO("Honey Updated", true, 3.0, Unit.SPOONS)
        )
    ),
    listOf(
        "Break eggs and add sugar updated.",
        "Mix until creamy updated.",
        "Add oil and milk and continue mixing updated.",
        "Add flour, baking powder, vanilla and salt updated.",
        "Mix until smooth updated.",
        "Slightly oil a pan and heat. Reduce heat once pan is hot updated.",
        "Pour mixture into pan updated.",
        "Cook until bubbles appear on top and flip to cook the other side updated.",
        "Cook all of the batter the same way updated.",
        "Pour honey on top and serve updated."
    )
)

val createPancakesDTO = CreateRecipeDTO(
    "Pancakes",
    4,
    mapOf(
        "For Batter" to listOf(
            MeasuredIngredientDTO("Egg", true, 2.0, Unit.QUANTITY),
            MeasuredIngredientDTO("Sugar", true, 0.5, Unit.CUPS),
            MeasuredIngredientDTO("Flour", true, 2.0, Unit.CUPS),
            MeasuredIngredientDTO("Milk", true, 1.5, Unit.CUPS),
            MeasuredIngredientDTO("Oil", true, 2.0, Unit.SPOONS),
            MeasuredIngredientDTO("Baking Powder", true, 1.0, Unit.PACKS),
            MeasuredIngredientDTO("Vanilla", true, 1.0, Unit.PACKS),
            MeasuredIngredientDTO("Salt", true, 1.0, Unit.PINCHES)
        ),
        "For Cooking" to listOf(
            MeasuredIngredientDTO("Oil", true, 2.0, Unit.SPOONS)
        ),
        "For Serving" to listOf(
            MeasuredIngredientDTO("Honey", true, 3.0, Unit.SPOONS)
        )
    ),
    listOf(
        "Break eggs and add sugar.",
        "Mix until creamy.",
        "Add oil and milk and continue mixing.",
        "Add flour, baking powder, vanilla and salt.",
        "Mix until smooth.",
        "Slightly oil a pan and heat. Reduce heat once pan is hot.",
        "Pour mixture into pan.",
        "Cook until bubbles appear on top and flip to cook the other side.",
        "Cook all of the batter the same way.",
        "Pour honey on top and serve."
    )
)

val updatePancakesDTO = UpdateRecipeDTO(
    "Pancakes Updated",
    3,
    mapOf(
        "For Batter Updated" to listOf(
            MeasuredIngredientDTO("Egg Updated", true, 2.0, Unit.QUANTITY),
            MeasuredIngredientDTO("Sugar Updated", true, 0.5, Unit.CUPS),
            MeasuredIngredientDTO("Flour Updated", true, 2.0, Unit.CUPS),
            MeasuredIngredientDTO("Milk Updated", true, 1.5, Unit.CUPS),
            MeasuredIngredientDTO("Oil Updated", true, 2.0, Unit.SPOONS),
            MeasuredIngredientDTO("Baking Powder Updated", true, 1.0, Unit.PACKS),
            MeasuredIngredientDTO("Vanilla Updated", true, 1.0, Unit.PACKS),
            MeasuredIngredientDTO("Salt Updated", true, 1.0, Unit.PINCHES)
        ),
        "For Cooking Updated" to listOf(
            MeasuredIngredientDTO("Oil Updated", true, 2.0, Unit.SPOONS)
        ),
        "For Serving Updated" to listOf(
            MeasuredIngredientDTO("Honey Updated", true, 3.0, Unit.SPOONS)
        )
    ),
    listOf(
        "Break eggs and add sugar updated.",
        "Mix until creamy updated.",
        "Add oil and milk and continue mixing updated.",
        "Add flour, baking powder, vanilla and salt updated.",
        "Mix until smooth updated.",
        "Slightly oil a pan and heat. Reduce heat once pan is hot updated.",
        "Pour mixture into pan updated.",
        "Cook until bubbles appear on top and flip to cook the other side updated.",
        "Cook all of the batter the same way updated.",
        "Pour honey on top and serve updated."
    )
)