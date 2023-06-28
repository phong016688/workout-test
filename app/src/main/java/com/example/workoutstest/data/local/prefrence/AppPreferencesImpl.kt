package com.example.workoutstest.data.local.prefrence

import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import java.util.concurrent.ConcurrentHashMap

interface AppSharedPreferences {
    fun <T> get(key: String, clazz: Class<T>): T?
    fun <T> put(key: String, data: T)
    fun clear()
    fun clearKey(key: String)
    fun <T : Any> observe(key: String, clazz: Class<T>): Flow<T>
}

@Suppress("UNCHECKED_CAST")
class AppPreferencesImpl (@ApplicationContext context: Context) : AppSharedPreferences {
    private val sharedPreferences by lazy {
        context.getSharedPreferences(AppPrefKey.APP_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private val mListeners = ConcurrentHashMap<OnSharedPreferenceChangeListener, Unit>()

    override fun <T> get(key: String, clazz: Class<T>): T? {
        return kotlin.runCatching { mapperData(key, clazz) }.getOrNull()
    }

    override fun <T> put(key: String, data: T) {
        sharedPreferences.edit().apply {
            when (data) {
                is String -> putString(key, data)
                is Boolean -> putBoolean(key, data)
                is Float -> putFloat(key, data)
                is Int -> putInt(key, data)
                is Long -> putLong(key, data)
                else -> putString(key, Gson().toJson(data))
            }
            apply()
        }
    }

    override fun clear() {
        sharedPreferences.edit().apply {
            clear()
            apply()
        }
    }

    override fun clearKey(key: String) {
        sharedPreferences.edit().apply {
            remove(key)
            apply()
        }
    }

    override fun <T : Any> observe(key: String, clazz: Class<T>): Flow<T> {
        return callbackFlow {
            val listener = OnSharedPreferenceChangeListener { _, changedKey ->
                if (changedKey == key) {
                    trySend(Unit)
                }
            }
            trySend(Unit)
            mListeners[listener] = Unit
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            awaitClose {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
                mListeners.remove(listener)
            }
        }.map { mapperData(key, clazz) }
    }

    private fun <T> mapperData(key: String, clazz: Class<T>): T {
        return when (clazz) {
            String::class.java -> sharedPreferences.getString(key, "").let { it as T }
            Boolean::class.java -> sharedPreferences.getBoolean(key, false).let { it as T }
            Float::class.java -> sharedPreferences.getFloat(key, 0f).let { it as T }
            Int::class.java -> sharedPreferences.getInt(key, 0).let { it as T }
            Long::class.java -> sharedPreferences.getLong(key, 0).let { it as T }
            else -> Gson().fromJson(sharedPreferences.getString(key, ""), clazz)
        }
    }
}