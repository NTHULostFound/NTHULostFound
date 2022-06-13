package ss.team16.nthulostfound.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ss.team16.nthulostfound.domain.model.UserData
import ss.team16.nthulostfound.domain.repository.UserRepository

class UserRepositoryImpl(
    private val appContext: Context
): UserRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")

    override suspend fun getUser(): UserData {
        val preferences = appContext.dataStore.data.first()
        return UserData(
            name = preferences[NAME] ?: "",
            studentId = preferences[STUDENT_ID] ?: "",
            email = preferences[EMAIL] ?: "",
        )
    }

    override suspend fun saveUser(user: UserData) {
        appContext.dataStore.edit { preference ->
            preference[NAME] = user.name
            preference[STUDENT_ID] = user.studentId
            preference[EMAIL] = user.email
        }
    }

    override fun getIsNotificationEnable(): Flow<Boolean> {
        return appContext.dataStore.data.map { preferences ->
            preferences[IS_NOTIFICATION_ENABLE] ?: true
        }
    }

    override fun getIsUserDataSet(): Flow<Boolean> {
        return appContext.dataStore.data.map {  preferences ->
            !(preferences[NAME].isNullOrBlank() && preferences[STUDENT_ID].isNullOrBlank())
        }
    }

    override suspend fun setIsNotificationEnable(status: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[IS_NOTIFICATION_ENABLE] = status
        }
    }

    override suspend fun getAccessToken(): String {
        val preferences = appContext.dataStore.data.first()

        return preferences[ACCESS_TOKEN] ?: "" // default empty string
    }

    override suspend fun saveAccessToken(token: String) {
        appContext.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }

    override fun getAvatarFilename(): Flow<String?> {
        return appContext.dataStore.data.map { preferences ->
            preferences[AVATAR_FILENAME]
        }
    }

    override suspend fun setAvatarFilename(filename: String) {
        appContext.dataStore.edit { preferences ->
            preferences[AVATAR_FILENAME] = filename
            Log.d("Avatar", "Set finish: $filename")
        }
    }

    override fun getShowPinMessage(): Flow<Int> {
        return appContext.dataStore.data.map { preferences ->
            preferences[SHOW_PIN_MESSAGE] ?: 0b11 // default show all
        }
    }

    override suspend fun setShowPinMessage(code: Int) {
        appContext.dataStore.edit { preferences ->
            preferences[SHOW_PIN_MESSAGE] = code
        }
    }

    override fun getIsReviewAsked(): Flow<Boolean> {
        return appContext.dataStore.data.map { preferences ->
            preferences[IS_REVIEW_ASKED] ?: false
        }
    }

    override suspend fun setIsReviewAsked(value: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[IS_REVIEW_ASKED] = value
        }
    }

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("user_access_token")

        val NAME = stringPreferencesKey("user_name")
        val STUDENT_ID = stringPreferencesKey("user_student_id")
        val EMAIL = stringPreferencesKey("user_email")

        val IS_NOTIFICATION_ENABLE = booleanPreferencesKey("user_is_notification_enable")

        val AVATAR_FILENAME = stringPreferencesKey("user_avatar_filename")

        val SHOW_PIN_MESSAGE = intPreferencesKey("user_show_pin_message")
        val IS_REVIEW_ASKED = booleanPreferencesKey("is_review_asked")
    }
}