package com.data.repositories

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.domain.datasources.IAppSettingsDataSource
import com.support.shared_pref.BaseLiveSharedPreferences
import com.support.shared_pref.Prefs


class AppSettingsRepository(
    private val applicationContext: Context,
    private val plainGson: Gson
) : IAppSettingsDataSource {

    private var liveSharedPreferences: BaseLiveSharedPreferences
    private val SP_NAME = "prefs_myapp" //TODO: RENAME

    companion object {

        private var INSTANCE: AppSettingsRepository? = null

        @JvmStatic
        fun getInstance(applicationContext: Context, plainGson: Gson): AppSettingsRepository {
            if (INSTANCE == null) {
                synchronized(AppSettingsRepository::javaClass) {
                    INSTANCE = AppSettingsRepository(applicationContext, plainGson)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }

    }

    init {
        Prefs.Builder()
            .setContext(applicationContext)
            .setMode(Context.MODE_PRIVATE)
            .setPrefsName(SP_NAME)
            .build()

        liveSharedPreferences = BaseLiveSharedPreferences(Prefs.getPreferences())
    }
}