package ss.team16.nthulostfound.domain.repository

import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.NewItemData

interface ItemRepository {
    fun getItems(): List<ItemData>

    suspend fun getItem(uuid: String): Result<ItemData>

    suspend fun newItem(item: NewItemData): Result<ItemData>

    suspend fun endItem(item: ItemData): Result<ItemData>

    suspend fun getContact(uuid: String): Result<String>
}