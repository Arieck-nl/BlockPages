package nl.arieck.blockpages.domain.features.character

import nl.arieck.blockpages.domain.features.character.models.Character

/**
 * Created by Rick van 't Hof on 16/06/2023.
 */
interface CharacterRepository {
    suspend fun getCharacters(page: Int): List<Character>?
    suspend fun getCharacter(id: String): Character
}