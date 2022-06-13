package ss.team16.nthulostfound.data.source

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemRemoteKeys

@Dao
interface ItemRemoteKeysDao {

    @Query("SELECT * FROM items_remote_keys WHERE uuid = :uuid")
    suspend fun getRemoteKey(uuid: String): ItemRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ItemRemoteKeys>)

    @Query("DELETE FROM items")
    suspend fun clearRemoteKeys()

}