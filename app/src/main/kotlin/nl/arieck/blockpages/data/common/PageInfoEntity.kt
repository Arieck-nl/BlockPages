package nl.arieck.blockpages.data.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageInfoEntity(
    @SerialName("count")
    val count: Int?,
    @SerialName("next")
    val next: String?,
    @SerialName("pages")
    val pages: Int?,
    @SerialName("prev")
    val prev: String?
)