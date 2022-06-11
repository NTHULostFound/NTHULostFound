package ss.team16.nthulostfound.data.source

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ss.team16.nthulostfound.domain.model.ItemData

@Dao
interface ItemsDao {

    @Query("SELECT * FROM items")
    fun getItems(): PagingSource<Int, ItemData>

    @Query("SELECT * FROM items WHERE uuid = :uuid")
    suspend fun getItem(uuid: String): ItemData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ItemData>)

    @Query("DELETE FROM items")
    suspend fun clearItems()

}