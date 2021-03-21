package com.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class RoomManager {

    companion object {

        private var INSTANCE: RoomDataSource? = null

        private val DB_NAME = "MyApp-DB" //TODO: RENAME

        @JvmStatic
        fun getInstance(applicationContext: Context): RoomDataSource {
            if (INSTANCE == null) {
                synchronized(RoomManager::javaClass) {
                    INSTANCE = create(applicationContext)
                }
            }
            return INSTANCE!!
        }

        private fun create(context: Context): RoomDataSource {
            return Room.databaseBuilder(
                context,
                RoomDataSource::class.java,
                DB_NAME
            ).addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                MIGRATION_3_4,
                MIGRATION_4_5,
                MIGRATION_5_6,
                MIGRATION_6_7
            ).allowMainThreadQueries().build()
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE tbl_contact ADD COLUMN is_system_user INTEGER DEFAULT 0 NOT NULL")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE tbl_contact ADD COLUMN user_presence_privacy TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE tbl_contact ADD COLUMN user_receipt_privacy TEXT DEFAULT '' NOT NULL")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DELETE FROM tbl_contact")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE tbl_user_groups ADD COLUMN broadcast_group INTEGER DEFAULT 0 NOT NULL")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE tbl_user_groups ADD COLUMN member_count INTEGER DEFAULT 0 NOT NULL")
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("UPDATE tbl_message_archive SET media_thumb=''")
            }
        }
    }
}