package ss.team16.nthulostfound.domain.model

import java.util.*

enum class ItemType {
    LOST, FOUND
}

data class ItemData(
    val type: ItemType = ItemType.FOUND,
    val uuid: String = "",
    val name: String = "",
    val description: String = "",
    val date: Date = Date(),
    val place: String = "",
    val how: String = "",
    val images: List<UploadedImage> = emptyList(),
    val isOwner: Boolean = false,
    val resolved: Boolean = false
)
