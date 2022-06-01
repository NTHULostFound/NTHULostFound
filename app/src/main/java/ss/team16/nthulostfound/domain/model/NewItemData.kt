package ss.team16.nthulostfound.domain.model

import java.util.*

enum class NewItemType {
    NEW_FOUND,
    NEW_LOST
}

data class NewItemData(
    val type: NewItemType,
    val name: String,
    val description: String?,
    val date: Date,
    val place: String,
    val how: String,
    val contact: String,
    val who: String?,
    val images: List<String> = emptyList(),
)
