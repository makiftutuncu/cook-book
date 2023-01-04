package dev.akif.cookbook.api

import dev.akif.cookbook.recipe.RecipeService
import dev.akif.cookbook.recipe.SearchParameters
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.json

@Component
class Handler(private val recipes: RecipeService) {
    suspend fun getAll(request: ServerRequest): ServerResponse {
        val searchParameters = request.queryParams().run {
            SearchParameters(
                name = getFirst("name"),
                vegetarian = getFirst("vegetarian")?.toBooleanStrictOrNull(),
                numberOfServings = getFirst("numberOfServings")?.toIntOrNull(),
                includes = get("includes")?.toSet(),
                excludes = get("excludes")?.toSet(),
                query = getFirst("query")
            )
        }

        return ServerResponse
            .ok()
            .json()
            .bodyValueAndAwait(
                recipes
                    .getAll(searchParameters)
                    .map(RecipeDTO::from)
            )
    }

    suspend fun create(request: ServerRequest): ServerResponse {
        val createRecipe = request.bodyToMono(CreateRecipeDTO::class.java).map { it.toCreateRecipe() }.awaitSingle()

        return ServerResponse
            .status(HttpStatus.CREATED)
            .json()
            .bodyValueAndAwait(RecipeDTO.from(recipes.create(createRecipe)))
    }

    suspend fun update(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val updateRecipe = request.bodyToMono(UpdateRecipeDTO::class.java).map { it.toUpdateRecipe() }.awaitSingle()

        return ServerResponse
            .ok()
            .json()
            .bodyValueAndAwait(RecipeDTO.from(recipes.update(id, updateRecipe)))
    }

    suspend fun delete(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()

        return ServerResponse
            .ok()
            .json()
            .bodyValueAndAwait(recipes.delete(id))
    }
}
