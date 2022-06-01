package ss.team16.nthulostfound.domain.usecase

import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.repository.ItemRepository

class GetItemUseCase(
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(uuid: String): ItemData? {
        return itemRepository.getItem(uuid)
    }
}