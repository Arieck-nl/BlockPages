package nl.arieck.blockpages.ui.features.character

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import nl.arieck.blockpages.domain.common.Failure
import nl.arieck.blockpages.ui.features.character.CharacterNavItem.Companion.route
import nl.arieck.blockpages.ui.features.character.detail.CharacterDetailScreen

/**
 * Created by Rick van 't Hof on 18/06/2023.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CharacterNav(onError: (Failure) -> Unit) {

    val navController = rememberAnimatedNavController()
    val characterActions = remember { CharacterNavActions(navController) }

    AnimatedNavHost(
        navController = navController,
        startDestination = CharacterNavItem.LIST.route,
        modifier = Modifier.fillMaxSize(),
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it } },
        popEnterTransition = { slideInHorizontally { -it } },
        popExitTransition = { slideOutHorizontally { it } }
    ) {

        composable(CharacterNavItem.LIST.route) {
            CharacterScreen(onError = onError, characterActions = characterActions)
        }
        composable(route = Uri.decode(with(characterActions) { buildDetailStartRoute() }.toString()),
            arguments = listOf(navArgument(CharacterNavActions.Extras.ID) {
                nullable = false
            }
            )) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString(CharacterNavActions.Extras.ID)
            CharacterDetailScreen(
                characterId = characterId.orEmpty(),
                onError = onError,
                characterActions = characterActions
            )
        }
    }
}

class CharacterNavActions(navController: NavHostController) {
    val toOverview: () -> Unit = {
        navController.navigate(CharacterNavItem.LIST.route)
    }

    val toDetail: (id: String) -> Unit = {
        navController.navigate(Uri.decode(buildDetailRoute(it).toString()))
    }

    fun buildDetailStartRoute(): Uri {
        return buildDetailRoute("""{${Extras.ID}}""")
    }

    private fun buildDetailRoute(id: String): Uri {
        val uriBuilder = Uri.Builder()
        uriBuilder.path(CharacterNavItem.DETAIL.route)
        uriBuilder.appendQueryParameter(Extras.ID, id)
        return uriBuilder.build()
    }

    val pop: () -> Boolean = {
        navController.popBackStack()
    }

    object Extras {
        const val ID = "id"
    }
}

enum class CharacterNavItem {
    LIST, DETAIL;

    companion object {
        val CharacterNavItem.route: String
            get() = this.toString().lowercase()
    }

}