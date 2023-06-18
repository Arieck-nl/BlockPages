package nl.arieck.blockpages.data.features.character

import io.ktor.resources.Resource

@Resource(CharacterResource.BASE_URL)
class CharacterResource(val page: Int = 1) {
    @Resource("new")
    class New(val parent: CharacterResource = CharacterResource())

    @Resource("{id}")
    class Id(val parent: CharacterResource = CharacterResource(), val id: Long) {
        @Resource("edit")
        class Edit(val parent: Id)
    }

    companion object{
        const val BASE_URL = "/api/character"
    }
}