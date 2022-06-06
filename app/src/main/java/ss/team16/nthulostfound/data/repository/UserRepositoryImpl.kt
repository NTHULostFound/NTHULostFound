package ss.team16.nthulostfound.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import ss.team16.nthulostfound.domain.model.UserData
import ss.team16.nthulostfound.domain.repository.UserRepository

class UserRepositoryImpl(
    val appContext: Context
): UserRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")

    override suspend fun getUser(): UserData {
        val preferences = appContext.dataStore.data.first()
        return UserData(
            uuid = preferences[UUID] ?: "",
            accessToken = preferences[ACCESS_TOKEN] ?: "",
            fcmToken = preferences[FCM_TOKEN] ?: "",
            name = preferences[NAME] ?: "",
            studentId = preferences[STUDENT_ID] ?: "",
            email = preferences[EMAIL] ?: "",
            isNotificationEnable = preferences[IS_NOTIFICATION_ENABLE] ?: false
        )
    }

    override suspend fun saveUser(user: UserData) {
        appContext.dataStore.edit { preference ->
            preference[UUID] = user.uuid
            preference[ACCESS_TOKEN] = user.accessToken
            preference[FCM_TOKEN] = user.fcmToken

            preference[NAME] = user.name
            preference[STUDENT_ID] = user.studentId
            preference[EMAIL] = user.email

            preference[IS_NOTIFICATION_ENABLE] = user.isNotificationEnable
        }
    }

    companion object {
        val UUID = stringPreferencesKey("user_uuid")
        val ACCESS_TOKEN = stringPreferencesKey("user_access_token")
        val FCM_TOKEN = stringPreferencesKey("user_fcm_token")

        val NAME = stringPreferencesKey("user_name")
        val STUDENT_ID = stringPreferencesKey("user_student_id")
        val EMAIL = stringPreferencesKey("user_email")

        val IS_NOTIFICATION_ENABLE = booleanPreferencesKey("user_is_notification_enable")
    }
}