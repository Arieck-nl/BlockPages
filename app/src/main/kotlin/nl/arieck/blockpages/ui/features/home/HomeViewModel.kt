package nl.arieck.blockpages.ui.features.home

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import nl.arieck.blockpages.domain.common.Failure
import nl.arieck.blockpages.domain.features.character.CharacterRepository
import nl.arieck.blockpages.domain.features.character.models.Character
import nl.arieck.blockpages.ui.common.toFailure
import org.koin.core.component.KoinComponent

/**
 * UI state for the Home screen
 */
data class HomeUiState(
    val failure: Failure? = null,
    val loading: Boolean = false
)

class HomeViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel(), KoinComponent, LifecycleEventObserver {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(HomeUiState(loading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val parkingReviewData: Flow<PagingData<Character>> = Pager(
        config = PagingConfig(pageSize = 20),
        initialKey = 1,
        pagingSourceFactory = { BlockPagingSource(characterRepository::getCharacters, ::loading, ::error) }
    ).flow.cachedIn(viewModelScope)

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {

            }

            else -> {}
        }
    }

    private fun error(failure: Failure) {
        _uiState.update { it.copy(failure = failure) }
    }

    private fun loading(currently: Boolean) {
        _uiState.update { it.copy(loading = currently) }
    }
}

class BlockPagingSource(
    val getPage: suspend (page: Int, limit: Int) -> List<Character>?,
    val loading: (Boolean) -> Unit,
    val onError: (Failure) -> Unit,
) : PagingSource<Int, Character>() {
    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        loading(true)
        val pageNumber = params.key ?: 1

        val page = runCatching {
            getPage(pageNumber, PAGE_LIMIT)
        }.onFailure {
            onError(it.toFailure())
        }.getOrNull().orEmpty()

        val prevKey = if (pageNumber > 0) pageNumber - 1 else null

        // This API defines that it's out of data when a page returns empty. When out of
        // data, we return `null` to signify no more pages should be loaded
        val nextKey = if (!page.isNullOrEmpty()) pageNumber + 1 else null
        loading(false)
        return LoadResult.Page(
            data = page,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

    companion object {
        const val PAGE_LIMIT = 20
    }
}
