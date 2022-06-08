package ss.team16.nthulostfound.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ss.team16.nthulostfound.domain.model.DateConverter
import ss.team16.nthulostfound.domain.model.ImagesConverter
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.ItemRemoteKeys

@Database(
    entities = [ItemData::class, ItemRemoteKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class, ImagesConverter::class)
abstract class ItemsDatabase : RoomDatabase() {

    abstract fun itemsDao(): ItemsDao
    abstract fun itemRemoteKeysDao(): ItemRemoteKeysDao

    companion object {
        const val DATABASE_NAME = "lost_and_found_db"
    }
}