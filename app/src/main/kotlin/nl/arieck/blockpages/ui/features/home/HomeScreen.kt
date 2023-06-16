package nl.arieck.blockpages.ui.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import nl.arieck.blockpages.domain.common.Failure
import nl.arieck.blockpages.domain.features.article.models.Article
import org.koin.androidx.compose.getViewModel

/**
 * Created by Rick van 't Hof on 16/06/2023.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = getViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onError: (failure: Failure) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val listItems: LazyPagingItems<Article> = viewModel.parkingReviewData.collectAsLazyPagingItems()

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(viewModel)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(viewModel)
        }
    }

    LaunchedEffect(uiState.failure) {
        uiState.failure?.let(onError)
    }


    HomeUi(uiState = uiState, listItems = listItems, onArticleClick = {})

}

@Composable
fun HomeUi(uiState: Any, listItems: LazyPagingItems<Article>, onArticleClick: () -> Unit) {
    TODO("Not yet implemented")
}
