package com.capstoneproject.mytravel.model


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[USERID_KEY] ?: 0,
                preferences[PHOTO_URL_KEY] ?:"",
                preferences[NAME_KEY] ?:"",
                preferences[EMAIL_KEY] ?:"",
                preferences[LOCATION_KEY] ?:"",
                preferences[AGE_KEY] ?:0,
                preferences[STATE_KEY] ?: false,
                preferences[TOKEN] ?:""
            )
        }
    }

    suspend fun login(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USERID_KEY] = user.userId
            preferences[PHOTO_URL_KEY] = user.photo_url
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[LOCATION_KEY] = user.location
            preferences[AGE_KEY] = user.age
            preferences[STATE_KEY] = user.isLogin
            preferences[TOKEN] = user.token
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
            preferences[TOKEN] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USERID_KEY = intPreferencesKey("userid")
        private val PHOTO_URL_KEY = stringPreferencesKey("photo_url")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val LOCATION_KEY = stringPreferencesKey("location")
        private val AGE_KEY = intPreferencesKey("age")
        private val STATE_KEY = booleanPreferencesKey("state")
        private val TOKEN = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}