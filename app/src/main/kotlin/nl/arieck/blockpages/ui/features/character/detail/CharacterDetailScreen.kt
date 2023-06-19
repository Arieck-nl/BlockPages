package nl.arieck.blockpages.ui.features.character.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import nl.arieck.blockpages.R
import nl.arieck.blockpages.domain.common.Failure
import nl.arieck.blockpages.domain.features.character.models.Character
import nl.arieck.blockpages.ui.features.character.CharacterNavActions
import nl.arieck.blockpages.ui.theme.BlockPagesTheme
import org.koin.androidx.compose.getViewModel

/**
 * Created by Rick van 't Hof on 16/06/2023.
 */
@Composable
fun CharacterDetailScreen(
    characterId: String,
    viewModel: CharacterDetailViewModel = getViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onError: (failure: Failure) -> Unit,
    characterActions: CharacterNavActions,
) {

    val uiState by viewModel.uiState.collectAsState()

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(viewModel)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(viewModel)
        }
    }

    LaunchedEffect(uiState.failure) {
        uiState.failure?.let(onError)
    }

    LaunchedEffect(characterId) {
        viewModel.provideCharacterId(characterId)
    }

    CharacterDetailUi(uiState = uiState, onBack = { characterActions.pop() })
}

@Composable
fun CharacterDetailUi(
    uiState: CharacterDetailUiState,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .navigationBarsPadding()
            .statusBarsPadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp)) {

            Image(
                painter = painterResource(id = androidx.appcompat.R.drawable.abc_ic_ab_back_material),
                modifier = Modifier
                    .clickable { onBack() }
                    .padding(top = 24.dp, bottom = 24.dp),
                contentDescription = null
            )


            uiState.character?.let { character ->

                val imgModifier = if (character.imageUrl == null) {
                    Modifier.height(64.dp)
                } else {
                    Modifier
                }

                AsyncImage(
                    model = character.imageUrl,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onBackground)
                        .then(imgModifier),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    listOf(
                        context.getString(R.string.character_detail_name_label) to character.name,
                        context.getString(R.string.character_detail_gender_label) to character.gender,
                        context.getString(R.string.character_detail_origin_label) to character.origin,
                        context.getString(R.string.character_detail_species_label) to character.species,
                        context.getString(R.string.character_detail_status_label) to character.status,
                        context.getString(R.string.character_detail_type_label) to character.type,
                    ).forEach { characterCharacteristic ->
                        characterCharacteristic.second?.let {
                            CharacterDetailRow(label = characterCharacteristic.first, value = it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterDetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(1f))
        Text(text = value, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
fun HomePreview() {
    BlockPagesTheme {

        CharacterDetailUi(
            uiState = CharacterDetailUiState(
                character = Character(
                    id = "1",
                    createdAt = "2022-09-27T05:57:09.607741",
                    name = "Morty",
                    imageUrl = null,
                    episode = listOf("S01E03", "S03E06"),
                    gender = "Unknown",
                    origin = "Unknown",
                    species = "Human",
                    status = "Hopelessly single",
                    type = "Extraordinary",
                ),
                loading = false
            ),
            onBack = {}
        )
    }
}
