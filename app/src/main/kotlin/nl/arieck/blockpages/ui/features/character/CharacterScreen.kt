package nl.arieck.blockpages.ui.features.character

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.flow.flowOf
import nl.arieck.blockpages.domain.common.Failure
import nl.arieck.blockpages.domain.features.character.models.Character
import nl.arieck.blockpages.ui.theme.BlockPagesTheme
import org.koin.androidx.compose.getViewModel
import kotlin.math.nextDown

/**
 * Created by Rick van 't Hof on 16/06/2023.
 */
@Composable
fun CharacterScreen(
    viewModel: CharacterViewModel = getViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onError: (failure: Failure) -> Unit,
    characterActions: CharacterNavActions,
) {

    val uiState by viewModel.uiState.collectAsState()
    val listItems: LazyPagingItems<Character> = viewModel.characterData.collectAsLazyPagingItems()

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(viewModel)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(viewModel)
        }
    }

    LaunchedEffect(uiState.failure) {
        uiState.failure?.let(onError)
    }


    CharacterUi(uiState = uiState, listItems = listItems, onCharacterClick = { characterActions.toDetail(it) })

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CharacterUi(
    uiState: CharacterUiState,
    listItems: LazyPagingItems<Character>?,
    onCharacterClick: (id: String) -> Unit,
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        LazyColumn(modifier = Modifier.statusBarsPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            listItems?.let { articlePage ->

                // dig into the snapshot list in order to add sticky headers
                // preferably do this in viewmodel, but we are a bit limited by the paging library
                articlePage.itemSnapshotList.items
                    .groupBy {
                        val currentValue = it.id.toIntOrNull() ?: 0
                        (currentValue / 10f).nextDown().toInt()
                    }
                    .forEach { (idGroup, charactersInIdGroup) ->

                        // pretty useless addition of stickyheader, just here for demonstration purposes
                        stickyHeader {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(0.85f))
                                    .padding(start = 24.dp, top = 8.dp, bottom = 8.dp, end = 24.dp),
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
                                text = "${(idGroup * 10) + 1}-${((idGroup + 1) * 10)}"
                            )
                        }


                        item {

                            com.google.accompanist.flowlayout.FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                mainAxisAlignment = MainAxisAlignment.SpaceEvenly,
                                mainAxisSpacing = 8.dp,
                                crossAxisSpacing = 8.dp
                            ) {

                                charactersInIdGroup.forEach { character ->
                                    articlePage[articlePage.itemSnapshotList.indexOf(character)]

                                    Box(modifier = Modifier
                                        .clickable {
                                            onCharacterClick(character.id)
                                        }
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .padding(8.dp)) {

                                        character.imageUrl?.let {
                                            AsyncImage(
                                                model = it,
                                                modifier = Modifier
                                                    .padding(16.dp),
                                                contentDescription = null
                                            )
                                        }
                                        Text(
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                                                .padding(16.dp),
                                            text = character.id,
                                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 30.sp),
                                            textAlign = TextAlign.End
                                        )
                                    }

                                }
                            }
                        }
                    }
            }
        }
    }
}

@Preview
@Composable
fun CharacterPreview() {
    BlockPagesTheme {

        CharacterUi(
            uiState = CharacterUiState(
                loading = false
            ), listItems = flowOf(
                PagingData.from(
                    listOf(
                        Character(
                            id = "1",
                            createdAt = "2022-09-27T05:57:09.607741",
                            name = "Morty",
                            imageUrl = null,
                        ),
                        Character(
                            id = "1",
                            createdAt = "2022-09-27T05:57:09.607741",
                            name = "Morty",
                            imageUrl = null,
                        ),
                    )
                )
            ).collectAsLazyPagingItems()
        ) { }
    }
}
