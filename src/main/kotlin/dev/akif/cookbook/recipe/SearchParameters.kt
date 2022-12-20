package dev.akif.cookbook.recipe

data class SearchParameters(
    val name: String?,
    val vegetarian: Boolean?,
    val numberOfServings: Int?,
    val includes: Set<String>?,
    val excludes: Set<String>?,
    val query: String?
)
