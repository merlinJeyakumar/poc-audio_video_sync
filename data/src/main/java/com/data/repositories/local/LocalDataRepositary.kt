package com.data.repositories.local

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import com.data.database.RoomDataSource
import com.data.repositories.BaseRepository
import com.domain.datasources.local.ILocalDataSource
import com.domain.entity.QuickTextEntity

class LocalDataRepositary(
    private val roomManager: RoomDataSource
) : BaseRepository(), ILocalDataSource {

    companion object {

        private var INSTANCE: LocalDataRepositary? = null

        @JvmStatic
        fun getInstance(
            roomManager: RoomDataSource
        ): LocalDataRepositary {
            if (INSTANCE == null) {
                synchronized(LocalDataRepositary::javaClass) {
                    INSTANCE =
                        LocalDataRepositary(roomManager)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }

    val quickTextItemDao = roomManager.quickTextItemDao

    override fun getLiveQuickText(): LiveData<List<QuickTextEntity>> {
        return quickTextItemDao.getLiveQuickTextList
    }

}
