package nl.arieck.blockpages.data.features.character.entities


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterLocationEntity(
    @SerialName("name")
    val name: String? = null,
    @SerialName("url")
    val url: String? = null
)