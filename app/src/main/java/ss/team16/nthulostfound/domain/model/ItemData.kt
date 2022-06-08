package ss.team16.nthulostfound.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

enum class ItemType {
    LOST, FOUND
}

@Entity(tableName = "items")
data class ItemData(
    @PrimaryKey(autoGenerate = false)
    val type: ItemType = ItemType.FOUND,
    val uuid: String = "",
    val name: String = "",
    val description: String? = null,
    val date: Date = Date(),
    val place: String = "",
    val how: String = "",
    val images: List<String> = emptyList(),
    val isOwner: Boolean = false,
    val resolved: Boolean = false
)
