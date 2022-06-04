package ss.team16.nthulostfound.domain.usecase

import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.domain.repository.ItemRepository

class GetItemsUseCase(
    private val itemRepository: ItemRepository
) {
    operator fun invoke(): List<ItemData> {
        return itemRepository.getItems()
    }
}