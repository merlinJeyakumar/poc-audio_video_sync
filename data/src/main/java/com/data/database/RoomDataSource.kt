package com.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.data.database.dao.*
import com.domain.entity.*

@Database(
    entities = arrayOf(
        QuickTextEntity::class
    ),
    version = 7,
    exportSchema = false
)
abstract class RoomDataSource : RoomDatabase() {

    abstract val quickTextItemDao: QuickTextItemDao


}
