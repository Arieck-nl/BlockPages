package nl.arieck.blockpages.data.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedResponseEntity<T>(
    @SerialName("info")
    val info: PageInfoEntity?,
    @SerialName("results")
    val results: List<T>?
)