package ss.team16.nthulostfound.domain.model

import androidx.room.Entity

@Entity(tableName = "items_remote_keys")
data class ItemRemoteKeys(
    val uuid: String,
    val cursor: String
)
