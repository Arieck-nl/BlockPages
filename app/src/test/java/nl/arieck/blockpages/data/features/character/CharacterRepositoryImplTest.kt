package nl.arieck.blockpages.data.features.character

/**
 * Created by Rick van 't Hof on 18/06/2023.
 */
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.headersOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import nl.arieck.blockpages.common.MainCoroutineRule
import nl.arieck.blockpages.data.features.character.entities.CharacterEntity
import nl.arieck.blockpages.di.appModules
import nl.arieck.blockpages.domain.features.character.CharacterRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get

/**
 * Created by Rick van 't Hof on 18/06/2023.
 */
@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class CharacterRepositoryImplTest : KoinTest {

    private lateinit var characterRepository: CharacterRepository

    lateinit var httpClient: HttpClient

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create { modules(appModules) }


    @Before
    fun setup() {

        httpClient = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    if (request.url.encodedPath == CharacterResource.BASE_URL) {
                        respond(
                            content = CharacterApiResponses.getCharacters,
                            headers = headersOf("Content-Type", "application/json")
                        )
                    } else error("Unhandled ${request.url}")
                }
            }
            // invoke default network settings
            get<HttpClientConfig<*>.() -> Unit>().invoke(this)
        }
        characterRepository = CharacterRepositoryImpl(httpClient)
    }

    @Test
    fun verifyRepositoryMapsEntitiesToDomain() {
        val page = 1
        val sample = CharacterEntity(id = 1, name = "Morty")


//         get is an inline function and can not be mocked, so we need to mock the api call
//        `when`(httpClient.get(CharacterResource(page)).body<List<CharacterEntity>>()).thenReturn(listOf(sample)
//        )
        runBlocking {
            val result = characterRepository.getCharacters(page)
            assert(result?.any { it.name.equals(sample.name) && it.id == sample.id.toString() } == true)
        }
    }
}