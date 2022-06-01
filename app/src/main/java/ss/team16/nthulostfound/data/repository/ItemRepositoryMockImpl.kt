package ss.team16.nthulostfound.data.repository

import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.repository.ItemRepository
import java.util.*

class ItemRepositoryMockImpl: ItemRepository {
    override fun getItems(): List<ItemData> {
        TODO("Not yet implemented")
    }

    override suspend fun getItem(uuid: String): ItemData? {
        return ItemData(
            "機率課本",
            "我的機率課本不見了，可能是上完課忘記帶走了，但我回去找之後就找不到了",
            Date(),
            "台達 105",
            "請連絡我取回"
        )
    }

    override suspend fun addItem(item: ItemData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteItem(item: ItemData) {
        TODO("Not yet implemented")
    }

    override suspend fun endItem(item: ItemData) {
        TODO("Not yet implemented")
    }

}