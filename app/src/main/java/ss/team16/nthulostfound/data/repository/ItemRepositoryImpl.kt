package ss.team16.nthulostfound.data.repository

import android.icu.text.SimpleDateFormat
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import ss.team16.nthulostfound.*
import ss.team16.nthulostfound.domain.model.*
import ss.team16.nthulostfound.domain.repository.ItemRepository
import java.util.*

class ItemRepositoryImpl(
    private val apolloClient: ApolloClient
): ItemRepository {
    override suspend fun getItems(
        type: ItemType,
        first: Int?,
        last: Int?,
        after: String?,
        before: String?
    ): Result<ItemsConnection> {

        val queryType =
            if (type == ItemType.FOUND)
                ss.team16.nthulostfound.type.ItemType.FOUND
            else
                ss.team16.nthulostfound.type.ItemType.LOST

        return try {
            val itemsQuery = ItemsQuery(
                type = queryType,
                first = Optional.presentIfNotNull(first),
                last = Optional.presentIfNotNull(last),
                after = Optional.presentIfNotNull(after),
                before = Optional.presentIfNotNull(before),
            )
            val response = apolloClient.query(itemsQuery).execute().dataAssertNoErrors

            val itemEdges = response.items.edges.map { edge ->
                ItemEdge(
                    item =
                        with (edge.node) {
                            val itemType =
                                if (edge.node.type == ss.team16.nthulostfound.type.ItemType.FOUND)
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
                        },
                    cursor = edge.cursor
                )
            }

            val itemsConnection = ItemsConnection(
                edges = itemEdges,
                hasNextPage = response.items.pageInfo.hasNextPage,
                hasPreviousPage = response.items.pageInfo.hasPreviousPage
            )

            Result.success(itemsConnection)
        } catch (e: Exception) {
            Result.failure(Exception(e))
        }
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