package ss.team16.nthulostfound.domain.usecase

import ss.team16.nthulostfound.domain.repository.ItemRepository

class GetContactUseCase(
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(uuid: String): String {
        return itemRepository.getContact(uuid)
    }
}