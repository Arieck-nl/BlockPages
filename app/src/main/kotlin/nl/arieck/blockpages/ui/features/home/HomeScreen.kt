package nl.arieck.blockpages.ui.features.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf
import nl.arieck.blockpages.domain.common.Failure
import nl.arieck.blockpages.domain.features.article.models.Article
import nl.arieck.blockpages.ui.common.toMonthDate
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeUi(uiState: HomeUiState, listItems: LazyPagingItems<Article>?, onArticleClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LazyColumn {
            listItems?.let { articlePage ->

                // dig into the snapshot list in order to add sticky headers
                // preferably do this in viewmodel, but we are a bit limited by the paging library
                articlePage.itemSnapshotList.items
                    .sortedByDescending { it.createdAt }
                    .groupBy { it.createdAt.toMonthDate() }
                    .forEach { (month, articlesInMonth) ->

                        stickyHeader {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background.copy(0.95f))
                                    .padding(top = 16.dp, bottom = 16.dp),
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
                                text = month.orEmpty().uppercase()
                            )
                        }

                        items(articlesInMonth) { article ->
                            articlePage[articlePage.itemSnapshotList.indexOf(article)]

                            Row(modifier = Modifier
                                .background(MaterialTheme.colorScheme.onBackground)
                                .padding(16.dp)) {
                                Text(modifier = Modifier.fillMaxWidth(), text = article.createdAt.orEmpty())
                            }

                        }
                    }
            }
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    HomeUi(
        uiState = HomeUiState(
            articles = listOf(
            ), loading = false
        ), listItems = flowOf(
            PagingData.from(
                listOf(
                    Article(id = "1", createdAt = "2022-09-27T05:57:09.607741"),
                    Article(id = "1", createdAt = "2022-09-27T05:57:09.607741"),
                    Article(id = "1", createdAt = "2022-09-27T05:57:09.607741"),
                )
            )
        ).collectAsLazyPagingItems()
    ) { }
}
