package dev.akif.cookbook.recipe;

import dev.akif.cookbook.ingredient.IngredientEntity;
import dev.akif.cookbook.ingredient.IngredientRepository;
import dev.akif.cookbook.ingredient.MeasuredIngredient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class RecipeService {
    private final RecipeRepository recipes;
    private final IngredientRepository ingredients;
    private final RecipeIngredientRepository recipeIngredients;
    private final RecipeInstructionRepository recipeInstructions;

    public Flux<Recipe> getAll(SearchParameters searchParameters) {
        log.info("Getting recipes with {}", searchParameters);

        final var getAllRecipes = Flux.fromIterable(
                recipes.findAllBy(
                        searchParameters.name(),
                        searchParameters.numberOfServings(),
                        Pageable.unpaged()
                )
        );

        return getAllRecipes.flatMap(recipe -> {
            final var recipeId = recipe.getId();

            final var getRecipeIngredients = Mono.fromCallable(() -> {
                log.debug("Getting ingredient ids of recipe {}", recipeId);
                return recipeIngredients.findByRecipeId(recipeId);
            });

            final var getInstructions = Mono.fromCallable(() -> {
                log.debug("Getting instructions of recipe {}", recipeId);
                return recipeInstructions.findByRecipeId(recipeId);
            });

            return Mono.zip(getRecipeIngredients, getInstructions).flatMap(tuple -> {
                final var recipeIngredients = tuple.getT1();
                final var instructions = tuple.getT2();

                final var getIngredients = Mono.fromCallable(() -> {
                    final var ingredientIds = recipeIngredients.stream().map(RecipeIngredientEntity::getIngredientId).collect(Collectors.toSet());
                    log.debug("Getting ingredients {} of recipe {}", ingredientIds, recipeId);
                    return ingredients.findAllByIdIn(ingredientIds);
                });

                return getIngredients.flatMap(ingredients -> toRecipe(recipe, recipeIngredients, ingredients, instructions, searchParameters));
            });
        });
    }

    private Mono<Recipe> toRecipe(
            RecipeEntity recipe,
            List<RecipeIngredientEntity> recipeIngredients,
            List<IngredientEntity> ingredients,
            List<RecipeInstructionEntity> instructions,
            SearchParameters searchParameters
    ) {
        final var recipeId = recipe.getId();

        if (!respectsVegetarian(ingredients, searchParameters)) {
            log.debug("Skipping recipe {} because it did respect vegetarian for {}", recipeId, searchParameters);
            return Mono.empty();
        }

        if (!containsAllOfIncludedIngredients(ingredients, searchParameters)) {
            log.debug("Skipping recipe {} because it did not contain all included ingredients for {}", recipeId, searchParameters);
            return Mono.empty();
        }

        if (!containsNoneOfExcludedIngredients(ingredients, searchParameters)) {
            log.debug("Skipping recipe {} because it contains some of excluded ingredients for {}", recipeId, searchParameters);
            return Mono.empty();
        }

        if (!instructionsContainQuery(instructions, searchParameters)) {
            log.debug("Skipping recipe {} because its instructions don't contain query for {}", recipeId, searchParameters);
            return Mono.empty();
        }

        return Mono.just(
                new Recipe(
                        recipeId,
                        recipe.getName(),
                        recipe.getNumberOfServings(),
                        groupIngredients(recipeIngredients, ingredients),
                        instructions.stream().map(RecipeInstructionEntity::getInstruction).toList()
                )
        );
    }

    private Map<String, List<MeasuredIngredient>> groupIngredients(
            List<RecipeIngredientEntity> recipeIngredients,
            List<IngredientEntity> ingredients
    ) {
        final var ingredientLookup = ingredients.stream().collect(Collectors.toMap(IngredientEntity::getId, i -> i));
        final var result = new HashMap<String, List<MeasuredIngredient>>();

        recipeIngredients.forEach(recipeIngredient -> {
            final var ingredientsForLabel = result.getOrDefault(recipeIngredient.getLabel(), new ArrayList<>());
            final var measuredIngredient = new MeasuredIngredient(
                    ingredientLookup.get(recipeIngredient.getIngredientId()).toIngredient(),
                    recipeIngredient.getValue(),
                    recipeIngredient.getUnit()
            );
            ingredientsForLabel.add(measuredIngredient);
            result.put(recipeIngredient.getLabel(), ingredientsForLabel);
        });

        return result;
    }

    private boolean respectsVegetarian(List<IngredientEntity> ingredients, SearchParameters searchParameters) {
        return searchParameters.vegetarian() == null
                || !searchParameters.vegetarian()
                || ingredients.stream().allMatch(IngredientEntity::getVegetarian);
    }

    private boolean containsAllOfIncludedIngredients(List<IngredientEntity> ingredients, SearchParameters searchParameters) {
        final var includedIngredientNames = toLowerCasedSet(searchParameters.includes());

        return includedIngredientNames.isEmpty() || ingredients
                .stream()
                .map(i -> i.getName().toLowerCase())
                .collect(Collectors.toSet())
                .containsAll(includedIngredientNames);
    }

    private boolean containsNoneOfExcludedIngredients(List<IngredientEntity> ingredients, SearchParameters searchParameters) {
        final var excludedIngredientNames = toLowerCasedSet(searchParameters.excludes());

        final var recipeIngredientNames = ingredients
                .stream()
                .map(i -> i.getName().toLowerCase())
                .collect(Collectors.toSet());

        return excludedIngredientNames.isEmpty() || recipeIngredientNames.stream().noneMatch(excludedIngredientNames::contains);
    }

    private boolean instructionsContainQuery(List<RecipeInstructionEntity> instructions, SearchParameters searchParameters) {
        return searchParameters.query() == null || instructions.stream().anyMatch(
                i -> i.getInstruction().toLowerCase().contains(searchParameters.query().toLowerCase())
        );
    }

    private Set<String> toLowerCasedSet(List<String> list) {
        return Optional
                .ofNullable(list)
                .map(HashSet::new)
                .orElse(new HashSet<>())
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}
