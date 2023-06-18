package nl.arieck.blockpages.ui.features.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import nl.arieck.blockpages.common.MainCoroutineRule
import nl.arieck.blockpages.domain.features.character.CharacterRepository
import nl.arieck.blockpages.domain.features.character.models.Character
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

/**
 * Created by Rick van 't Hof on 18/06/2023.
 */
@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class HomeViewModelTest {

    private lateinit var homeViewModel: HomeViewModel

    private val characterRepository: CharacterRepository = mock()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        homeViewModel = HomeViewModel(characterRepository)
    }

    @Test
    fun verifyCharacterSnapshotCallsRepository() {
        val characterList = listOf(
            Character("1", null, null, null),
            Character("2", null, null, null)
        )
        runBlocking {
            Mockito.`when`(characterRepository.getCharacters(1)).thenReturn(characterList)

            val result = homeViewModel.characterData.asSnapshot()
            assertEquals(result, characterList)
        }
    }

    @Test
    fun verifyCharacterSnapshotDoesNotCallRepositoryOnEmptyResult() {
        val characterList = listOf(
            Character("1", null, null, null),
            Character("2", null, null, null)
        )
        runBlocking {
            Mockito.`when`(characterRepository.getCharacters(1)).thenReturn(null)

            val result = homeViewModel.characterData.asSnapshot()
            assertNotEquals(result, characterList)
        }
    }

}