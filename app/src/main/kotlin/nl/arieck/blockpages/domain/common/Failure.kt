package nl.arieck.blockpages.domain.common

import androidx.annotation.Keep
import androidx.annotation.StringRes
import nl.arieck.blockpages.R
import nl.arieck.blockpages.domain.common.Failure.FeatureFailure

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
@Keep
sealed class Failure(val message: String? = null, val cause: Throwable? = null) {
    @Keep
    class NetworkConnection(message: String? = null) : Failure(message)
    object RetryError : Failure()
    object NotFoundError : Failure()
    object UnAuthorizedError : Failure()

    /** * Extend this class for feature specific failures.*/
    @Keep
    abstract class FeatureFailure : Failure()

}
@StringRes
fun Failure.commonRes(): Int{
    return when(this){
        is Failure.RetryError -> R.string.error_retry
        is Failure.NotFoundError -> R.string.error_not_found
        else -> R.string.error_unknown
    }
}
