
package nl.arieck.blockpages.domain.common

import androidx.annotation.Keep
import nl.arieck.blockpages.domain.common.Failure.FeatureFailure

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
@Keep
sealed class Failure(val message: String? = null, val cause: Throwable? = null) {
    @Keep
    class NetworkConnection(message: String? = null) : Failure(message)

    /** * Extend this class for feature specific failures.*/
    @Keep
    abstract class FeatureFailure : Failure()

}
