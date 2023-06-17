package nl.arieck.blockpages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import nl.arieck.blockpages.domain.common.Failure
import nl.arieck.blockpages.domain.common.commonRes
import nl.arieck.blockpages.ui.features.home.HomeScreen
import nl.arieck.blockpages.ui.theme.BlockPagesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            BlockPagesTheme {
                var errorState by remember { mutableStateOf<Failure?>(null) }
                val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }


                // A surface container using the 'background' color from the theme
                HomeScreen(onError = {})


                LaunchedEffect(errorState) {


                    errorState?.let {
                        val result = snackbarHostState.showSnackbar(
                            message = getString(it.commonRes()),
                            actionLabel = getString(R.string.general_ok).uppercase(),
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                /* action has been performed */
                                errorState = null
                            }

                            SnackbarResult.Dismissed -> {
                                /* dismissed, no action needed */
                                errorState = null
                            }
                        }
                    }
                }

            }
        }
    }
}