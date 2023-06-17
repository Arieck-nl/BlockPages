package nl.arieck.blockpages.data.features.character

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import nl.arieck.blockpages.data.common.PagedResponseEntity
import nl.arieck.blockpages.data.features.character.entities.CharacterEntity
import nl.arieck.blockpages.domain.features.character.CharacterRepository
import nl.arieck.blockpages.domain.features.character.models.Character

class CharacterRepositoryImpl(private val httpClient: HttpClient) : CharacterRepository {
    override suspend fun getCharacters(page: Int, limit: Int): List<Character>? {
        val entities: List<CharacterEntity>? =
            httpClient.use { client ->
                client.get(CharacterResource()).body<PagedResponseEntity<CharacterEntity>>().results
            }

        return entities?.map { Character(it.id.toString(), it.createdAt) }
    }
}