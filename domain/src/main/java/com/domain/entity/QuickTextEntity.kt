package com.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = QuickTextEntity.TABLE_NAME)
open class QuickTextEntity : com.support.room.BaseEntity() {

    companion object {
        const val TABLE_NAME = "tbl_quick_text"
    }

    object Fields {
        const val ID = "id"
        const val TEXT = "text"
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Fields.ID)
    var id: Long = 0

    @ColumnInfo(name = Fields.TEXT)
    var text: String = ""
}