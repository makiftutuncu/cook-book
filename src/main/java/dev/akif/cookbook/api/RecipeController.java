package dev.akif.cookbook.api;

import dev.akif.cookbook.recipe.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipes;

    @GetMapping
    public Flux<RecipeDTO> getAll() {
        log.debug("RecipeController.getAll");
        return recipes.getAll().map(RecipeDTO::from);
    }
}
