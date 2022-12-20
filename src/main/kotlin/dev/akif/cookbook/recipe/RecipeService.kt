package dev.akif.cookbook.recipe

import dev.akif.cookbook.ingredient.IngredientEntity
import dev.akif.cookbook.ingredient.IngredientRepository
import dev.akif.cookbook.ingredient.MeasuredIngredient
import dev.akif.cookbook.recipe.ingredient.RecipeIngredientEntity
import dev.akif.cookbook.recipe.ingredient.RecipeIngredientRepository
import dev.akif.cookbook.recipe.instruction.RecipeInstructionEntity
import dev.akif.cookbook.recipe.instruction.RecipeInstructionRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@OptIn(FlowPreview::class)
@Service
class RecipeService(
    private val recipes: RecipeRepository,
    private val ingredients: IngredientRepository,
    private val recipeIngredients: RecipeIngredientRepository,
    private val recipeInstructions: RecipeInstructionRepository
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(RecipeService::class.java)
    }

    suspend fun getAll(searchParameters: SearchParameters): List<Recipe> {
        log.info("Getting recipes with {}", searchParameters)

        return recipes
            .findAllBy(searchParameters.name, searchParameters.numberOfServings)
            .flatMapConcat { recipeEntity ->
                val recipeId = recipeEntity.id ?: -1

                log.debug("Getting ingredient ids of recipe {}", recipeId);
                val recipeIngredients = recipeIngredients.findByRecipeId(recipeId).toList()

                log.debug("Getting instructions of recipe {}", recipeId);
                val recipeInstructions = recipeInstructions.findByRecipeId(recipeId).toList()

                val ingredientIds = recipeIngredients.map { it.ingredientId }.toSet()

                log.debug("Getting ingredients {} of recipe {}", ingredientIds, recipeId)
                val ingredients = ingredients.findAllByIdIn(ingredientIds).toList()

                val recipe = toRecipe(
                    recipeEntity = recipeEntity,
                    recipeIngredients = recipeIngredients,
                    ingredients = ingredients,
                    instructions = recipeInstructions,
                    searchParameters = searchParameters
                )

                recipe?.let { flowOf(it) } ?: emptyFlow()
            }
            .toList()
    }

    suspend fun create(createRecipe: CreateRecipe): Recipe {
        log.info("Creating a new recipe {}", createRecipe)

        val recipe = recipes.save(
            RecipeEntity(
                name = createRecipe.name,
                numberOfServings = createRecipe.numberOfServings
            )
        )

        val recipeId: Long = recipe.id ?: -1

        val allIngredientNames = createRecipe.ingredients.values.flatMap { list -> list.map { it.name } }.toSet()

        log.debug("Finding existing ingredients for names {}", allIngredientNames)
        val existingIngredients = ingredients.findAllByNameIn(allIngredientNames).toList().associateBy { it.name }

        log.debug("Creating non-existing ingredients for names {}", allIngredientNames - existingIngredients.values.map { it.name }.toSet())
        val createdIngredients = ingredients.saveAll(
            createRecipe
                .ingredients
                .values
                .flatMap { list -> list.filter { existingIngredients[it.name] == null } }
                .map { IngredientEntity(name = it.name, vegetarian = it.vegetarian) }
                .distinctBy { it.name }
        ).toList().associateBy { it.name }

        val ingredientsByName = existingIngredients + createdIngredients

        log.debug("Creating recipe ingredients for recipe {}", recipeId)
        val recipeIngredients = this.recipeIngredients.saveAll(
            createRecipe.ingredients.flatMap { (label, measuredIngredients) ->
                measuredIngredients.mapIndexed { index, ingredient ->
                    RecipeIngredientEntity(
                        recipeId = recipeId,
                        ingredientId = ingredientsByName[ingredient.name]?.id ?: -1,
                        label = label,
                        value = ingredient.value,
                        unit = ingredient.unit,
                        sortOrder = index
                    )
                }
            }
        ).toList()

        log.debug("Creating recipe instructions for recipe {}", recipeId)
        val recipeInstructions = this.recipeInstructions.saveAll(
            createRecipe.instructions.mapIndexed { index, instruction ->
                RecipeInstructionEntity(
                    recipeId = recipeId,
                    sortOrder = index,
                    instruction = instruction
                )
            }
        ).toList()

        return Recipe(
            id = recipeId,
            name = recipe.name,
            numberOfServings = recipe.numberOfServings,
            ingredients = ingredientsByLabel(recipeIngredients, ingredientsByName.values.associateBy { it.id ?: -1 }),
            instructions = recipeInstructions.map { it.instruction }
        )
    }

    private fun ingredientsByLabel(
        recipeIngredients: List<RecipeIngredientEntity>,
        ingredientsById: Map<Long, IngredientEntity>
    ): Map<String, List<MeasuredIngredient>> =
        recipeIngredients.fold(mapOf()) { result, recipeIngredient ->
            val measuredIngredient = ingredientsById[recipeIngredient.ingredientId]
                ?.toIngredient()
                ?.let { MeasuredIngredient(it, recipeIngredient.value, recipeIngredient.unit) }

            if (measuredIngredient == null) {
                result
            } else {
                val ingredientsForLabel = result[recipeIngredient.label]
                if (ingredientsForLabel == null) {
                    result + (recipeIngredient.label to listOf(measuredIngredient))
                } else {
                    result + (recipeIngredient.label to (ingredientsForLabel + measuredIngredient))
                }
            }
        }

    private fun toRecipe(
        recipeEntity: RecipeEntity,
        recipeIngredients: List<RecipeIngredientEntity>,
        ingredients: List<IngredientEntity>,
        instructions: List<RecipeInstructionEntity>,
        searchParameters: SearchParameters,
    ): Recipe? {
        val recipeId: Long = recipeEntity.id ?: -1

        if (true == searchParameters.vegetarian && ingredients.any { !it.vegetarian }) {
            log.debug("Skipping recipe {} because it did respect vegetarian", recipeId)
            return null
        }

        val ingredientNames = ingredients.map { it.name.lowercase() }.toSet()
        val includedIngredientNames = searchParameters.includes?.map { it.lowercase() }?.toSet() ?: emptySet()

        if (includedIngredientNames.intersect(ingredientNames) != includedIngredientNames) {
            log.debug(
                "Skipping recipe {} because it did not contain all included ingredients for {}",
                recipeId,
                searchParameters.includes
            )
            return null
        }

        val excludedIngredientNames = searchParameters.excludes?.map { it.lowercase() }?.toSet() ?: emptySet()

        if (excludedIngredientNames.intersect(ingredientNames).isNotEmpty()) {
            log.debug(
                "Skipping recipe {} because it contains some of excluded ingredients for {}",
                recipeId,
                searchParameters.excludes
            )
            return null
        }

        if (false == searchParameters.query?.let { q -> instructions.any { it.instruction.contains(q, ignoreCase = true) } }) {
            log.debug(
                "Skipping recipe {} because its instructions don't contain query for '{}'",
                recipeId,
                searchParameters.query
            )
            return null
        }

        val ingredientsById = ingredients.groupingBy { it.id ?: -1 }.aggregate { _, _: IngredientEntity?, i, _ -> i }

        val labeledIngredients = ingredientsByLabel(recipeIngredients, ingredientsById)

        return Recipe(
            id = recipeId,
            name = recipeEntity.name,
            numberOfServings = recipeEntity.numberOfServings,
            ingredients = labeledIngredients,
            instructions = instructions.map { it.instruction }.toList()
        )
    }
}
