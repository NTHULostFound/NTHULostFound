package ss.team16.nthulostfound.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.domain.model.ItemsConnection
import ss.team16.nthulostfound.domain.model.NewItemData

interface ItemRepository {

    fun getPagingItems(
        type: ItemType,
        search: String? = null,
        myItems: Boolean = false
    ): Flow<PagingData<ItemData>>

    suspend fun getItems(
        type: ItemType,
        first: Int? = null,
        last: Int? = null,
        after: String? = null,
        before: String? = null,
        search: String? = null,
        mine: Boolean = false
    ): Result<ItemsConnection>

    suspend fun getItem(uuid: String): Result<ItemData>

    suspend fun newItem(item: NewItemData): Result<ItemData>

    suspend fun endItem(item: ItemData): Result<ItemData>

    suspend fun deleteItem(item: ItemData): Result<ItemData>

    suspend fun getContact(uuid: String): Result<String>
}