package dev.akif.cookbook.api;

import dev.akif.cookbook.recipe.RecipeService;
import dev.akif.cookbook.recipe.SearchParameters;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

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
}
