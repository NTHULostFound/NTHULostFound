package ss.team16.nthulostfound.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
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

object DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}

object ImagesConverter {
    @TypeConverter
    fun toImages(imagesStr: String?): List<String>? {
        return imagesStr?.split(",")
    }

    @TypeConverter
    fun fromImages(images: List<String>?): String? {
        return images?.joinToString(",")
    }
}