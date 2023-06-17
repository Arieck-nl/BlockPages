package nl.arieck.blockpages.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import nl.arieck.blockpages.BuildConfig
import nl.arieck.blockpages.domain.common.Failure

fun ViewModel.launchAndLoad(
    onLoading: (Boolean) -> Unit = {},
    onError: (Failure) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
): Job {
    onLoading(true)
    return this.viewModelScope.launch(Dispatchers.IO) {
        kotlin.runCatching {
            block()
        }.onFailure {
            it.toFailure()
        }
        onLoading(false)
    }
}

fun Throwable.toFailure(): Failure {
    if (BuildConfig.DEBUG) {
        this.printStackTrace()
    }

    if (this is ResponseException) {
        val error = when (this) {
            is ServerResponseException -> Failure.RetryError
            is ClientRequestException -> Failure.NotFoundError
            else -> throw this
        }
        return error
    } else {
        throw this
    }
}