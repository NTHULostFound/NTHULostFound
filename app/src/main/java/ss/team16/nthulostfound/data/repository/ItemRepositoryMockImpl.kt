package ss.team16.nthulostfound.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.domain.model.ItemsConnection
import ss.team16.nthulostfound.domain.model.NewItemData
import ss.team16.nthulostfound.domain.repository.ItemRepository
import java.util.*

class ItemRepositoryMockImpl: ItemRepository {
    override fun getPagingItems(
        type: ItemType,
        search: String?,
        myItems: Boolean
    ): Flow<PagingData<ItemData>> {
        TODO("Not yet implemented")
    }

    override suspend fun getItems(
        type: ItemType,
        first: Int?,
        last: Int?,
        after: String?,
        before: String?,
        search: String?,
        mine: Boolean
    ): Result<ItemsConnection> {
        TODO("Not yet implemented")
    }

    override suspend fun getItem(uuid: String): Result<ItemData> {
        return Result.success(
            ItemData(
                type = ItemType.LOST,
                name = "機率課本",
                description = "我的機率課本不見了，可能是上完課忘記帶走了，但我回去找之後就找不到了",
                date = Date(),
                place = "台達 105",
                how = "請連絡我取回"
            )
        )
    }

    override suspend fun newItem(item: NewItemData): Result<ItemData> {
        TODO("Not yet implemented")
    }

    override suspend fun endItem(item: ItemData): Result<ItemData> {
        TODO("Not yet implemented")
    }

    override suspend fun getContact(uuid: String): Result<String> {
        return Result.success(
            "FB: NTHULostFound" + "\n" +
                    "TG: @NTHULostFound"
        )
    }

}