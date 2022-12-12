package dev.akif.cookbook.recipe;

import dev.akif.cookbook.ingredient.IngredientEntity;
import dev.akif.cookbook.ingredient.IngredientRepository;
import dev.akif.cookbook.ingredient.MeasuredIngredient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public Flux<Recipe> getAll() {
        log.debug("RecipeService.getAll");
        return Flux.fromIterable(recipes.findAll()).flatMap(this::toRecipe);
    }

    public Mono<Recipe> toRecipe(RecipeEntity recipe) {
        final var getRecipeIngredients = Mono.fromCallable(() -> recipeIngredients.findByRecipeId(recipe.getId()));
        final var getInstructions = Mono.fromCallable(() -> recipeInstructions.findByRecipeId(recipe.getId()));

        return Mono.zip(getRecipeIngredients, getInstructions).flatMap(tuple -> {
            final var recipeIngredients = tuple.getT1();
            final var instructions = tuple.getT2();

            final var ingredientIds = recipeIngredients
                    .stream()
                    .map(RecipeIngredientEntity::getIngredientId)
                    .collect(Collectors.toSet());

            final var getIngredients = Mono.fromCallable(() -> ingredients
                    .findAllByIdIn(ingredientIds)
                    .stream()
                    .collect(Collectors.toMap(IngredientEntity::getId, i -> i)));

            return getIngredients.map(ingredients -> {
                final var ingredientsMap = new HashMap<String, List<MeasuredIngredient>>();

                recipeIngredients.forEach(recipeIngredient -> {
                    final var ingredientsForLabel = ingredientsMap.getOrDefault(recipeIngredient.getLabel(), new ArrayList<>());
                    final var measuredIngredient = new MeasuredIngredient(
                            ingredients.get(recipeIngredient.getIngredientId()).toIngredient(),
                            recipeIngredient.getValue(),
                            recipeIngredient.getUnit()
                    );
                    ingredientsForLabel.add(measuredIngredient);
                    ingredientsMap.put(recipeIngredient.getLabel(), ingredientsForLabel);
                });

                final var instructionList = instructions
                        .stream()
                        .map(RecipeInstructionEntity::getInstruction)
                        .toList();

                return new Recipe(recipe.getId(), recipe.getName(), recipe.getNumberOfServings(), ingredientsMap, instructionList);
            });
        });
    }
}
