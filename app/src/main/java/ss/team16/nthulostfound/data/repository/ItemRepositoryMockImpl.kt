package ss.team16.nthulostfound.data.repository

import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.domain.repository.ItemRepository
import java.util.*

class ItemRepositoryMockImpl: ItemRepository {
    override fun getItems(): List<ItemData> {
        TODO("Not yet implemented")
    }

    override suspend fun getItem(uuid: String): ItemData? {
        return ItemData(
            type = ItemType.LOST,
            name = "機率課本",
            description = "我的機率課本不見了，可能是上完課忘記帶走了，但我回去找之後就找不到了",
            date = Date(),
            place = "台達 105",
            how = "請連絡我取回"
        )
    }

    override suspend fun addItem(item: ItemData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteItem(item: ItemData) {
        TODO("Not yet implemented")
    }

    override suspend fun endItem(item: ItemData) {
        // TODO: Not yet implemented
    }

    override suspend fun getContact(uuid: String): String {
        return "FB: NTHULostFound" + "\n" +
                "TG: @NTHULostFound"
    }

}