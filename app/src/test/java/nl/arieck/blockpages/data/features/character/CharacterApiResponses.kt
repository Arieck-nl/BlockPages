package nl.arieck.blockpages.data.features.character

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.arieck.blockpages.data.common.PageInfoEntity
import nl.arieck.blockpages.data.common.PagedResponseEntity
import nl.arieck.blockpages.data.features.character.entities.CharacterEntity

/**
 * Created by Rick van 't Hof on 18/06/2023.
 */
object CharacterApiResponses {
    val getCharacters = Json.encodeToString(
        PagedResponseEntity(
            PageInfoEntity(null, null, null, null),
            listOf(CharacterEntity(id = 1, name = "Morty"))
        )
    )
}
