package ss.team16.nthulostfound.domain.usecase

import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.repository.ItemRepository

class DeleteItemUseCase(
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(item: ItemData): Result<ItemData> {
        return itemRepository.deleteItem(item)
    }
}