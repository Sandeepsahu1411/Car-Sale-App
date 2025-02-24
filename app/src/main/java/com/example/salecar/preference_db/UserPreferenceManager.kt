package com.example.salecar.preference_db

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_pref")

class UserPreferenceManager(private val context: Context) {

    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    val userID: Flow<String?> = context.dataStore.data.map {
        it[USER_ID_KEY]
    }

    suspend fun saveUserID(userID: String) {
        context.dataStore.edit {
            it[USER_ID_KEY] = userID
        }
    }

    suspend fun clearUserID() {
        context.dataStore.edit {
            it.remove(USER_ID_KEY)
        }

    }


}