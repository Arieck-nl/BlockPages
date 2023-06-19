package nl.arieck.blockpages.data.features.character

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import nl.arieck.blockpages.data.common.PagedResponseEntity
import nl.arieck.blockpages.data.features.character.entities.CharacterEntity
import nl.arieck.blockpages.domain.features.character.CharacterRepository
import nl.arieck.blockpages.domain.features.character.models.Character

class CharacterRepositoryImpl(private val httpClient: HttpClient) : CharacterRepository {
    override suspend fun getCharacters(page: Int): List<Character>? {
        val entities: List<CharacterEntity>? =
            httpClient.get(CharacterResource(page)).body<PagedResponseEntity<CharacterEntity>>().results

        return entities?.map {
            it.toDomain()
        }
    }

    override suspend fun getCharacter(id: String): Character {
        return httpClient.get(CharacterResource.Id(id = id)).body<CharacterEntity>().toDomain()
    }
}

fun CharacterEntity.toDomain(): Character {
    return Character(
        id = id.toString(),
        createdAt = created,
        name = name,
        imageUrl = image,
        episode = episode,
        gender = gender,
        origin = origin?.name,
        species = species,
        status = status,
        type = type,
    )
}