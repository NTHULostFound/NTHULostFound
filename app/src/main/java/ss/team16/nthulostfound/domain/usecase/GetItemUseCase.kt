package ss.team16.nthulostfound.domain.usecase

import ss.team16.nthulostfound.data.source.ItemsDatabase
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.repository.ItemRepository

class GetItemUseCase(
    private val itemRepository: ItemRepository,
    private val itemsDatabase: ItemsDatabase
) {
    suspend operator fun invoke(uuid: String): Result<ItemData> {
        val databaseItem = itemsDatabase.itemsDao().getItem(uuid)
        if (databaseItem != null)
            return Result.success(databaseItem)

        return itemRepository.getItem(uuid)
    }
}