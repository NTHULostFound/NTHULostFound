package ss.team16.nthulostfound.domain.repository

import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.NewItemData

interface ItemRepository {
    fun getItems(): List<ItemData>

    suspend fun getItem(uuid: String): ItemData?

    suspend fun newItem(item: NewItemData)

    suspend fun endItem(item: ItemData)

    suspend fun getContact(uuid: String): String
}