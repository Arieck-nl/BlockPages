package nl.arieck.blockpages.data.features.character.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Rick van 't Hof on 16/06/2023.
 */
@Serializable
data class CharacterEntity(
    @SerialName("created")
    val created: String? = null,
    @SerialName("episode")
    val episode: List<String>? = null,
    @SerialName("gender")
    val gender: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("image")
    val image: String? = null,
    @SerialName("location")
    val location: CharacterLocationEntity? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("origin")
    val origin: CharacterOriginEntity? = null,
    @SerialName("species")
    val species: String? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("url")
    val url: String? = null
)