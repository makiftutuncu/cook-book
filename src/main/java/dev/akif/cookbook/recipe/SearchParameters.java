package dev.akif.cookbook.recipe;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record SearchParameters(
        String name,
        Boolean vegetarian,
        Integer numberOfServings,
        List<String> includes,
        List<String> excludes,
        String query
) {
}
