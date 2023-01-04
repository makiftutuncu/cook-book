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
import java.lang.IllegalArgumentException

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
        log.info("Getting recipes with $searchParameters")

        return recipes
            .findAllBy(searchParameters.name, searchParameters.numberOfServings)
            .flatMapConcat { recipeEntity ->
                val recipeId = recipeEntity.id ?: -1

                log.debug("Getting ingredient ids of recipe $recipeId")
                val recipeIngredients = recipeIngredients.findByRecipeId(recipeId).toList()

                log.debug("Getting instructions of recipe $recipeId")
                val recipeInstructions = recipeInstructions.findByRecipeId(recipeId).toList()

                val ingredientIds = recipeIngredients.map { it.ingredientId }.toSet()

                log.debug("Getting ingredients $ingredientIds of recipe $recipeId")
                val ingredients = ingredients.findAllByIdIn(ingredientIds).toList()

                val recipe = filterRecipe(recipeEntity, ingredients, recipeIngredients, recipeInstructions, searchParameters)

                recipe?.let { flowOf(it) } ?: emptyFlow()
            }
            .toList()
    }

    suspend fun create(createRecipe: CreateRecipe): Recipe {
        log.info("Creating a new recipe $createRecipe")
        val recipe = recipes.save(RecipeEntity(name = createRecipe.name, numberOfServings = createRecipe.numberOfServings))

        val recipeId: Long = recipe.id ?: -1

        val (ingredients, recipeIngredients) = saveIngredients(recipeId, createRecipe.toLabelledMeasuredIngredients())

        val recipeInstructions = saveInstructions(recipeId, createRecipe.instructions)

        return toRecipe(recipe, ingredients, recipeIngredients, recipeInstructions)
    }

    suspend fun update(id: Long, updateRecipe: UpdateRecipe): Recipe {
        log.info("Updating recipe $id as $updateRecipe")

        val recipe = recipes.findById(id) ?: throw IllegalArgumentException("Recipe $id is not found!")

        val recipeId: Long = recipe.id ?: -1

        val (ingredients, recipeIngredients) = saveIngredients(recipeId, updateRecipe.toLabelledMeasuredIngredients())

        val recipeInstructions = saveInstructions(recipeId, updateRecipe.instructions)

        return toRecipe(recipe, ingredients, recipeIngredients, recipeInstructions)
    }

    private fun filterRecipe(
        recipeEntity: RecipeEntity,
        ingredients: List<IngredientEntity>,
        recipeIngredients: List<RecipeIngredientEntity>,
        recipeInstructions: List<RecipeInstructionEntity>,
        searchParameters: SearchParameters
    ): Recipe? {
        val recipeId: Long = recipeEntity.id ?: -1

        if (true == searchParameters.vegetarian && ingredients.any { !it.vegetarian }) {
            log.debug("Skipping recipe $recipeId because it did respect vegetarian")
            return null
        }

        val ingredientNames = ingredients.map { it.name.lowercase() }.toSet()
        val includedIngredientNames = searchParameters.includes?.map { it.lowercase() }?.toSet() ?: emptySet()

        if (includedIngredientNames.intersect(ingredientNames) != includedIngredientNames) {
            log.debug("Skipping recipe $recipeId because it did not contain all included ingredients for ${searchParameters.includes}")
            return null
        }

        val excludedIngredientNames = searchParameters.excludes?.map { it.lowercase() }?.toSet() ?: emptySet()

        if (excludedIngredientNames.intersect(ingredientNames).isNotEmpty()) {
            log.debug("Skipping recipe $recipeId because it contains some of excluded ingredients for ${searchParameters.excludes}")
            return null
        }

        if (false == searchParameters.query?.let { q -> recipeInstructions.any { it.instruction.contains(q, ignoreCase = true) } }) {
            log.debug("Skipping recipe $recipeId because its instructions don't contain query for '${searchParameters.query}'")
            return null
        }

        return toRecipe(recipeEntity, ingredients, recipeIngredients, recipeInstructions)
    }

    private fun toRecipe(
        recipe: RecipeEntity,
        ingredients: List<IngredientEntity>,
        recipeIngredients: List<RecipeIngredientEntity>,
        recipeInstructions: List<RecipeInstructionEntity>
    ): Recipe {
        val ingredientsById = ingredients.associateBy { it.id ?: -1 }

        val labelledIngredients = recipeIngredients.fold(mapOf<String, List<MeasuredIngredient>>()) { result, recipeIngredient ->
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

        return Recipe(
            id = recipe.id ?: -1,
            name = recipe.name,
            numberOfServings = recipe.numberOfServings,
            ingredients = labelledIngredients,
            instructions = recipeInstructions.map { it.instruction }
        )
    }

    private suspend fun saveIngredients(
        recipeId: Long,
        givenIngredients: Map<String, List<MeasuredIngredient>>
    ): Pair<List<IngredientEntity>, List<RecipeIngredientEntity>> {
        val allIngredientNames = givenIngredients.values.flatMap { list -> list.map { it.ingredient.name } }.toSet()

        log.debug("Finding existing ingredients for names $allIngredientNames")
        val existingIngredients = ingredients.findAllByNameIn(allIngredientNames).toList().associateBy { it.name }

        log.debug("Creating non-existing ingredients for names ${allIngredientNames - existingIngredients.values.map { it.name }.toSet()}")
        val createdIngredients = ingredients.saveAll(
            givenIngredients
                .values
                .flatMap { list ->
                    list.map { measuredIngredient ->
                        existingIngredients[measuredIngredient.ingredient.name]?.apply {
                            vegetarian = measuredIngredient.ingredient.vegetarian
                        } ?: IngredientEntity(name = measuredIngredient.ingredient.name, vegetarian = measuredIngredient.ingredient.vegetarian)
                    }
                }.distinctBy { it.name }
        ).toList().associateBy { it.name }

        val ingredientsByName = existingIngredients + createdIngredients

        log.debug("Deleting existing recipe ingredients for recipe $recipeId")
        recipeIngredients.deleteAllByRecipeId(recipeId).singleOrNull()

        log.debug("Saving recipe ingredients for recipe $recipeId")
        val recipeIngredients = recipeIngredients.saveAll(
            givenIngredients.flatMap { (label, measuredIngredients) ->
                measuredIngredients.mapIndexed { index, measuredIngredient ->
                    RecipeIngredientEntity(
                        recipeId = recipeId,
                        ingredientId = ingredientsByName[measuredIngredient.ingredient.name]?.id ?: -1,
                        label = label,
                        value = measuredIngredient.value,
                        unit = measuredIngredient.unit,
                        sortOrder = index
                    )
                }
            }
        ).toList()

        return ingredientsByName.values.toList() to recipeIngredients
    }

    private suspend fun saveInstructions(recipeId: Long, givenInstructions: List<String>): List<RecipeInstructionEntity> {
        log.debug("Deleting existing recipe instructions for recipe $recipeId")
        recipeInstructions.deleteAllByRecipeId(recipeId).singleOrNull()

        log.debug("Saving new recipe instructions for recipe $recipeId")
        return recipeInstructions.saveAll(
            givenInstructions.mapIndexed { index, instruction ->
                RecipeInstructionEntity(
                    recipeId = recipeId,
                    sortOrder = index,
                    instruction = instruction
                )
            }
        ).toList()
    }
}
