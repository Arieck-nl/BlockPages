package nl.arieck.blockpages.ui.features.character.detail

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import nl.arieck.blockpages.domain.common.Failure
import nl.arieck.blockpages.domain.features.character.CharacterRepository
import nl.arieck.blockpages.domain.features.character.models.Character
import nl.arieck.blockpages.ui.common.launchAndLoad
import org.koin.core.component.KoinComponent

data class CharacterDetailUiState(
    val character: Character? = null,
    val failure: Failure? = null,
    val loading: Boolean = false
)

class CharacterDetailViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel(), KoinComponent, LifecycleEventObserver {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(CharacterDetailUiState(loading = false))
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                updateCharacter()
            }

            else -> {}
        }
    }

    fun provideCharacterId(id: String) {
        if (_uiState.value.character?.id != id) {
            _uiState.update { it.copy(character = Character(id, null, null, null)) }
            updateCharacter()
        }
    }

    private fun updateCharacter() {
        _uiState.value.character?.id?.let {
            launchAndLoad(::loading, ::error) {
                val character = characterRepository.getCharacter(it)
                _uiState.update { it.copy(character = character) }
            }
        }
    }

    private fun error(failure: Failure) {
        _uiState.update { it.copy(failure = failure) }
    }

    private fun loading(currently: Boolean) {
        _uiState.update { it.copy(loading = currently) }
    }

}
