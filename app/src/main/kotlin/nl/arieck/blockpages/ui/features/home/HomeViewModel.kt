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
import nl.arieck.blockpages.domain.features.article.ArticleRepository
import nl.arieck.blockpages.domain.features.article.models.Article
import org.koin.core.component.KoinComponent

/**
 * UI state for the Home screen
 */
data class HomeUiState(
    val articles: List<Article>? = null,
    val failure: Failure? = null,
    val loading: Boolean = false
)

class HomeViewModel(
    private val articleRepository: ArticleRepository
) : ViewModel(), KoinComponent, LifecycleEventObserver {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(HomeUiState(loading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val parkingReviewData: Flow<PagingData<Article>> = Pager(
        config = PagingConfig(pageSize = 20),
        initialKey = 1,
        pagingSourceFactory = { BlockPagingSource(articleRepository::getArticles, ::loading) }
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

class BlockPagingSource(val getPage: (page: Int, limit: Int) -> List<Article>, val loading: (Boolean) -> Unit) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        loading(true)
        val pageNumber = params.key ?: 1

        val page = getPage(pageNumber, PAGE_LIMIT)

        val prevKey = if (pageNumber > 0) pageNumber - 1 else null

        // This API defines that it's out of data when a page returns empty. When out of
        // data, we return `null` to signify no more pages should be loaded
        val nextKey = if (page.isNotEmpty()) pageNumber + 1 else null
        loading(false)
        return LoadResult.Page(
            data = page,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

    companion object{
        const val PAGE_LIMIT = 20
    }
}