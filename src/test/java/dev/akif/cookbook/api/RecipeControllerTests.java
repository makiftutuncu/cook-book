package dev.akif.cookbook.api;

import dev.akif.cookbook.ingredient.CreateMeasuredIngredient;
import dev.akif.cookbook.ingredient.Ingredient;
import dev.akif.cookbook.ingredient.MeasuredIngredient;
import dev.akif.cookbook.ingredient.Unit;
import dev.akif.cookbook.recipe.Recipe;
import dev.akif.cookbook.recipe.RecipeService;
import dev.akif.cookbook.recipe.SearchParameters;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebFluxTest
class RecipeControllerTests {
    @Autowired
    private WebTestClient testClient;

    @MockBean
    private RecipeService recipeService;

    private final Recipe pancakesRecipe = new Recipe(
            1L,
            "Pancakes",
            4,
            Map.of(
                    "For Batter", List.of(
                            new MeasuredIngredient(new Ingredient(1, "Egg", true), 2, Unit.QUANTITY),
                            new MeasuredIngredient(new Ingredient(2, "Sugar", true), 0.5, Unit.CUPS),
                            new MeasuredIngredient(new Ingredient(3, "Flour", true), 2, Unit.CUPS),
                            new MeasuredIngredient(new Ingredient(4, "Milk", true), 1.5, Unit.CUPS),
                            new MeasuredIngredient(new Ingredient(5, "Oil", true), 2, Unit.SPOONS),
                            new MeasuredIngredient(new Ingredient(6, "Baking Powder", true), 1, Unit.PACKS),
                            new MeasuredIngredient(new Ingredient(7, "Vanilla", true), 1, Unit.PACKS),
                            new MeasuredIngredient(new Ingredient(8, "Salt", true), 1, Unit.PINCHES)
                    ),
                    "For Cooking", List.of(
                            new MeasuredIngredient(new Ingredient(5, "Oil", true), 2, Unit.SPOONS)
                    ),
                    "For Serving", List.of(
                            new MeasuredIngredient(new Ingredient(9, "Honey", true), 1, Unit.SPOONS)
                    )
            ),
            List.of(
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
    );

    private final RecipeDTO pancakesRecipeDTO = new RecipeDTO(
            1L,
            "Pancakes",
            4,
            Map.of(
                    "For Batter", List.of(
                            new MeasuredIngredientDTO("Egg", true, 2, Unit.QUANTITY),
                            new MeasuredIngredientDTO("Sugar", true, 0.5, Unit.CUPS),
                            new MeasuredIngredientDTO("Flour", true, 2, Unit.CUPS),
                            new MeasuredIngredientDTO("Milk", true, 1.5, Unit.CUPS),
                            new MeasuredIngredientDTO("Oil", true, 2, Unit.SPOONS),
                            new MeasuredIngredientDTO("Baking Powder", true, 1, Unit.PACKS),
                            new MeasuredIngredientDTO("Vanilla", true, 1, Unit.PACKS),
                            new MeasuredIngredientDTO("Salt", true, 1, Unit.PINCHES)
                    ),
                    "For Cooking", List.of(
                            new MeasuredIngredientDTO("Oil", true, 2, Unit.SPOONS)
                    ),
                    "For Serving", List.of(
                            new MeasuredIngredientDTO("Honey", true, 1, Unit.SPOONS)
                    )
            ),
            List.of(
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
    );

    private final CreateRecipeDTO createPancakesDTO = new CreateRecipeDTO(
            "Pancakes",
            4,
            Map.of(
                    "For Batter", List.of(
                            new MeasuredIngredientDTO("Egg", true, 2, Unit.QUANTITY),
                            new MeasuredIngredientDTO("Sugar", true, 0.5, Unit.CUPS),
                            new MeasuredIngredientDTO("Flour", true, 2, Unit.CUPS),
                            new MeasuredIngredientDTO("Milk", true, 1.5, Unit.CUPS),
                            new MeasuredIngredientDTO("Oil", true, 2, Unit.SPOONS),
                            new MeasuredIngredientDTO("Baking Powder", true, 1, Unit.PACKS),
                            new MeasuredIngredientDTO("Vanilla", true, 1, Unit.PACKS),
                            new MeasuredIngredientDTO("Salt", true, 1, Unit.PINCHES)
                    ),
                    "For Cooking", List.of(
                            new MeasuredIngredientDTO("Oil", true, 2, Unit.SPOONS)
                    ),
                    "For Serving", List.of(
                            new MeasuredIngredientDTO("Honey", true, 1, Unit.SPOONS)
                    )
            ),
            List.of(
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
    );

    @Test
    void gettingRecipes() {
        Mockito.when(
                recipeService.getAll(new SearchParameters(null, null, null, null, null, null))
        ).thenReturn(Flux.just(pancakesRecipe));

        testClient.get()
                .uri("/recipes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<RecipeDTO>>() {
                })
                .value(recipes -> {
                    assertEquals(1, recipes.size());
                    assertEquals(pancakesRecipeDTO, recipes.get(0));
                });
    }

    @Test
    void creatingRecipe() {
        Mockito.when(
                recipeService.create(
                        createPancakesDTO.name(),
                        createPancakesDTO.numberOfServings(),
                        Map.of(
                                "For Batter", List.of(
                                        new CreateMeasuredIngredient("Egg", true, 2, Unit.QUANTITY),
                                        new CreateMeasuredIngredient("Sugar", true, 0.5, Unit.CUPS),
                                        new CreateMeasuredIngredient("Flour", true, 2, Unit.CUPS),
                                        new CreateMeasuredIngredient("Milk", true, 1.5, Unit.CUPS),
                                        new CreateMeasuredIngredient("Oil", true, 2, Unit.SPOONS),
                                        new CreateMeasuredIngredient("Baking Powder", true, 1, Unit.PACKS),
                                        new CreateMeasuredIngredient("Vanilla", true, 1, Unit.PACKS),
                                        new CreateMeasuredIngredient("Salt", true, 1, Unit.PINCHES)
                                ),
                                "For Cooking", List.of(
                                        new CreateMeasuredIngredient("Oil", true, 2, Unit.SPOONS)
                                ),
                                "For Serving", List.of(
                                        new CreateMeasuredIngredient("Honey", true, 1, Unit.SPOONS)
                                )
                        ),
                        createPancakesDTO.instructions()
                )
        ).thenReturn(Mono.just(pancakesRecipe));

        testClient.post()
                .uri("/recipes")
                .body(Mono.just(createPancakesDTO), CreateRecipeDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(RecipeDTO.class)
                .value(recipe -> {
                    assertEquals(pancakesRecipeDTO, recipe);
                });
    }
}
