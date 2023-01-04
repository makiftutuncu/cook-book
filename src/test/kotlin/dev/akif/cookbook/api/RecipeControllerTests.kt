package dev.akif.cookbook.api

import dev.akif.cookbook.*
import dev.akif.cookbook.ingredient.IngredientRepository
import dev.akif.cookbook.recipe.RecipeRepository
import dev.akif.cookbook.recipe.ingredient.RecipeIngredientRepository
import dev.akif.cookbook.recipe.instruction.RecipeInstructionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.core.publisher.Mono

@ExperimentalCoroutinesApi
@AutoConfigureWebTestClient
@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class RecipeControllerTests(
    @Autowired private val testClient: WebTestClient,
    @Autowired private val recipes: RecipeRepository,
    @Autowired private val ingredients: IngredientRepository,
    @Autowired private val recipeIngredients: RecipeIngredientRepository,
    @Autowired private val recipeInstructions: RecipeInstructionRepository
) {
    companion object {
        @Container
        val postgresql = PostgreSQLContainer<Nothing>("postgres:15.1-alpine")

        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.flyway.url") { postgresql.jdbcUrl }
            registry.add("spring.r2dbc.url") { "r2dbc:postgresql://${postgresql.host}:${postgresql.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT)}/${postgresql.databaseName}" }
            registry.add("spring.r2dbc.username") { postgresql.username }
            registry.add("spring.r2dbc.password") { postgresql.password }
        }
    }

    @Order(1)
    @Test
    fun gettingRecipes() = runTest {
        testClient.get()
            .uri("/recipes")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(object : ParameterizedTypeReference<List<RecipeDTO>>() {})
            .value { recipes: List<RecipeDTO> ->
                assertEquals(1, recipes.size)
                assertEquals(pancakesRecipeDTO, recipes[0].copy(id = pancakesRecipeDTO.id))
            }
    }

    @Order(2)
    @Test
    fun creatingRecipe() = runTest {
        recipeInstructions.deleteAll()
        recipeIngredients.deleteAll()
        ingredients.deleteAll()
        recipes.deleteAll()

        testClient.post()
            .uri("/recipes")
            .body(Mono.just(createPancakesDTO), CreateRecipeDTO::class.java)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isCreated
            .expectBody(RecipeDTO::class.java)
            .value { recipe: RecipeDTO -> assertEquals(pancakesRecipeDTO, recipe.copy(id = pancakesRecipeDTO.id)) }
    }

    @Order(3)
    @Test
    fun updatingRecipe() = runTest {
        val recipeId = recipes.findAllBy(null, null).first().id!!

        testClient.put()
            .uri("/recipes/$recipeId")
            .body(Mono.just(updatePancakesDTO), UpdateRecipeDTO::class.java)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(RecipeDTO::class.java)
            .value { recipe: RecipeDTO -> assertEquals(updatedPancakesRecipeDTO.copy(id = recipeId), recipe) }
    }
}
