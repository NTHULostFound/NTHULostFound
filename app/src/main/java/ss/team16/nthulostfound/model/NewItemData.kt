package ss.team16.nthulostfound.model

import java.util.*

enum class NewItemType {
    NEW_FOUND,
    NEW_LOST
}

data class NewItemData(
    var type: NewItemType,
    var name: String = "",
    var description: String = "",
    var date: Date = Date(),
    var place: String = "",
    var how: String = "",
    var images: List<String> = emptyList(),
    var contact: String = "",
    var who: String = ""
)
