package ss.team16.nthulostfound.data.repository

import android.icu.text.SimpleDateFormat
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import ss.team16.nthulostfound.EndItemMutation
import ss.team16.nthulostfound.ItemContactQuery
import ss.team16.nthulostfound.ItemQuery
import ss.team16.nthulostfound.NewItemMutation
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.domain.model.NewItemData
import ss.team16.nthulostfound.domain.model.NewItemType
import ss.team16.nthulostfound.domain.repository.ItemRepository
import java.util.*

class ItemRepositoryImpl(
    private val apolloClient: ApolloClient
): ItemRepository {
    override fun getItems(): List<ItemData> {
        TODO("Not yet implemented")
    }

    override suspend fun getItem(uuid: String): Result<ItemData> {
        return try {
            val response = apolloClient.query(ItemQuery(uuid)).execute().dataAssertNoErrors

            val itemData =
                with (response.item) {
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

            Result.success(itemData)
        } catch (e: Exception) {
            Result.failure(Exception(e))
        }
    }

    override suspend fun newItem(item: NewItemData): Result<ItemData> {
        return try {
            lateinit var response: NewItemMutation.Data

            with (item) {
                val itemType =
                    if (type == NewItemType.NEW_FOUND)
                        ss.team16.nthulostfound.type.ItemType.FOUND
                    else
                        ss.team16.nthulostfound.type.ItemType.LOST

                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val dateString = format.format(date)

                val newItemMutation = NewItemMutation(
                    type = itemType,
                    name = name,
                    date = dateString,
                    place = place,
                    how = how,
                    images = images,
                    contact = contact,
                    who = Optional.presentIfNotNull(who),
                    description = Optional.presentIfNotNull(description)
                )

                response = apolloClient.mutation(newItemMutation).execute().dataAssertNoErrors
            }

            val itemData =
                with (response.newItem) {
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

            Result.success(itemData)
        } catch (e: Exception) {
            Result.failure(Exception(e))
        }
    }

    override suspend fun endItem(item: ItemData): Result<ItemData> {
        return try {
            val response = apolloClient.mutation(EndItemMutation(item.uuid)).execute().dataAssertNoErrors

            val itemData =
                with (response.endItem) {
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
            Result.success(itemData)
        } catch (e: Exception) {
            Result.failure(Exception(e))
        }
    }

    override suspend fun getContact(uuid: String): Result<String> {
        return try {
            val response = apolloClient.query(ItemContactQuery(uuid)).execute().dataAssertNoErrors

            Result.success(response.itemContact.contact)
        } catch (e: Exception) {
            Result.failure(Exception(e))
        }
    }

}