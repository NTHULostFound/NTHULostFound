package ss.team16.nthulostfound.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import ss.team16.nthulostfound.domain.model.NotificationData

@Database(
    entities = [NotificationData::class],
    version = 1
)
abstract class NotificationDatabase : RoomDatabase() {
    abstract val dao: NotificationDao
    companion object {
        const val DATABASE_NAME = "lost_and_found_db"
    }
}
