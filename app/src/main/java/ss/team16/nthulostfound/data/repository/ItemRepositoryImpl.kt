package ss.team16.nthulostfound.data.repository

import android.icu.text.SimpleDateFormat
import com.apollographql.apollo3.ApolloClient
import ss.team16.nthulostfound.ItemQuery
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.domain.model.NewItemData
import ss.team16.nthulostfound.domain.repository.ItemRepository
import java.util.*

class ItemRepositoryImpl(
    private val apolloClient: ApolloClient
): ItemRepository {
    override fun getItems(): List<ItemData> {
        TODO("Not yet implemented")
    }

    override suspend fun getItem(uuid: String): ItemData? {
        val response = apolloClient.query(ItemQuery(uuid)).execute()

        val itemData = response.data?.let {
            with (it.item) {
                val itemType =
                    if (type == ss.team16.nthulostfound.type.ItemType.FOUND)
                        ItemType.FOUND
                    else
                        ItemType.LOST

                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val dateString = date.toString()

                ItemData(
                    type = itemType,
                    uuid = uuid,
                    name = name,
                    description = description,
                    date = format.parse(dateString),
                    place = place,
                    how = how,
                    images = images,
                    isOwner = isMine,
                    resolved = resolved
                )
            }
        }

        return itemData
    }

    override suspend fun newItem(item: NewItemData) {
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