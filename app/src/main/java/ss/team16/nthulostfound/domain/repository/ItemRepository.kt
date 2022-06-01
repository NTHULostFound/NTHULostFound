package ss.team16.nthulostfound.domain.repository

import ss.team16.nthulostfound.domain.model.ItemData

interface ItemRepository {
    fun getItems(): List<ItemData>

    suspend fun getItem(uuid: String): ItemData?

    suspend fun addItem(item: ItemData)

    suspend fun deleteItem(item: ItemData)

    suspend fun endItem(item: ItemData)
}