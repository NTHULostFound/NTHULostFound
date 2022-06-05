package ss.team16.nthulostfound.data.source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ss.team16.nthulostfound.domain.model.NotificationData

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notification ORDER BY timestamp DESC")
    fun getNotifications() : Flow<List<NotificationData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNotification(notif: NotificationData)

    @Update
    suspend fun updateNotification(notif: NotificationData)

    @Delete
    suspend fun deleteNotification(notif: NotificationData)
}