package ss.team16.nthulostfound.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items_remote_keys")
data class ItemRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val uuid: String,
    val cursor: String
)
