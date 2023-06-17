package nl.arieck.blockpages.data.features.character.entities

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

/**
 * Created by Rick van 't Hof on 16/06/2023.
 */
@Serializable
@Resource("/articles")
data class CharacterEntity(val id: Int, val createdAt: String? = null)