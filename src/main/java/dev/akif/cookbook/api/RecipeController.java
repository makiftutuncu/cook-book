package dev.akif.cookbook.api;

import dev.akif.cookbook.recipe.RecipeService;
import dev.akif.cookbook.recipe.SearchParameters;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipes;

    @GetMapping
    public Flux<RecipeDTO> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean vegetarian,
            @RequestParam(required = false) Integer numberOfServings,
            @RequestParam(required = false) List<String> includes,
            @RequestParam(required = false) List<String> excludes,
            @RequestParam(required = false) String query
    ) {
        final var searchParameters = new SearchParameters(
                name,
                vegetarian,
                numberOfServings,
                includes,
                excludes,
                query
        );

        return recipes.getAll(searchParameters).map(RecipeDTO::from);
    }

    @PostMapping
    public Mono<RecipeDTO> create(@RequestBody CreateRecipeDTO createRecipe) {
        final var ingredients = createRecipe
                .ingredients()
                .entrySet()
                .stream()
                .map(e ->
                        Map.entry(
                                e.getKey(),
                                e
                                        .getValue()
                                        .stream()
                                        .map(MeasuredIngredientDTO::toCreateMeasuredIngredient)
                                        .toList()
                        )
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return recipes
                .create(
                        createRecipe.name(),
                        createRecipe.numberOfServings(),
                        ingredients,
                        createRecipe.instructions()
                )
                .map(RecipeDTO::from);
    }
}
