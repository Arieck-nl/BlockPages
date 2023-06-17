package nl.arieck.blockpages.data.features.character

import io.ktor.resources.Resource

@Resource("/api/character")
class CharacterResource {
    @Resource("new")
    class New(val parent: CharacterResource = CharacterResource())

    @Resource("{id}")
    class Id(val parent: CharacterResource = CharacterResource(), val id: Long) {
        @Resource("edit")
        class Edit(val parent: Id)
    }
}