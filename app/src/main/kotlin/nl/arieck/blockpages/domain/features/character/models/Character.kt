package nl.arieck.blockpages.domain.features.character.models

/**
 * Created by Rick van 't Hof on 16/06/2023.
 */
data class Character(
    val id: String,
    val createdAt: String?,
    val name: String?,
    val imageUrl: String?,
    val episode: List<String>? = null,
    val gender: String? = null,
    val origin: String? = null,
    val species: String? = null,
    val status: String? = null,
    val type: String? = null,
)