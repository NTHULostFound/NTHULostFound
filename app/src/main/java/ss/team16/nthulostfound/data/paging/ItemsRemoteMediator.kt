package ss.team16.nthulostfound.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ss.team16.nthulostfound.data.source.ItemsDatabase
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemRemoteKeys
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.domain.repository.ItemRepository
import java.lang.Error
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ItemsRemoteMediator(
    private val itemsDatabase: ItemsDatabase,
    private val itemsRepository: ItemRepository,
    private val type: ItemType,
    private val search: String? = null,
    private val myItems: Boolean = false
) : RemoteMediator<Int, ItemData>() {

    private val itemsDao = itemsDatabase.itemsDao()
    private val itemRemoteKeysDao = itemsDatabase.itemRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ItemData>
    ): MediatorResult {
        return try {
            val itemsResult = when (loadType) {
                LoadType.REFRESH -> {
                    itemsRepository.getItems(
                        type = type,
                        first = ITEMS_PER_PAGE,
                        search = search,
                        mine = myItems
                    )
                }
                LoadType.PREPEND -> {
                    val key = getRemoteKeyForFirstItem(state)
                    itemsRepository.getItems(
                        type = type,
                        last = ITEMS_PER_PAGE,
                        before = key?.cursor,
                        search = search,
                        mine = myItems
                    )
                }
                LoadType.APPEND -> {
                    val key = getRemoteKeyForLastItem(state)
                    itemsRepository.getItems(
                        type = type,
                        first = ITEMS_PER_PAGE,
                        after = key?.cursor,
                        search = search,
                        mine = myItems
                    )
                }
            }


            itemsResult.fold(
                onSuccess = { itemConnection ->
                    val endOfPaginationReached = !itemConnection.hasNextPage

                    itemsDatabase.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            itemsDao.clearItems()
                            itemRemoteKeysDao.clearRemoteKeys()
                        }

                        val items = itemConnection.edges.map { edge -> edge.item }

                        val keys = itemConnection.edges.map { edge ->
                            ItemRemoteKeys(
                                uuid = edge.item.uuid,
                                cursor = edge.cursor
                            )
                        }

                        itemsDao.insertAll(items)
                        itemRemoteKeysDao.insertAll(keys)
                    }

                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                },
                onFailure = {
                    MediatorResult.Error(it)
                }
            )
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, ItemData>
    ): ItemRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { item ->
                itemRemoteKeysDao.getRemoteKey(uuid = item.uuid)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, ItemData>
    ): ItemRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { item ->
                itemRemoteKeysDao.getRemoteKey(uuid = item.uuid)
            }
    }

    companion object {
        const val ITEMS_PER_PAGE = 10
    }

}