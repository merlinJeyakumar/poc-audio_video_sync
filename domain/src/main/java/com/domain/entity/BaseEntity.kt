package com.domain.entity

import androidx.room.*


open class BaseEntity {

    object Fields {
        /*const val ID = "id"*/
        const val IS_DELETED = "is_deleted"
        const val IS_ACTIVE = "is_active"
        const val CREATE_AT = "create_at"
        const val UPDATE_AT = "update_at"
    }

    /*@PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Fields.ID)
    var id: Long = 0*/

    @ColumnInfo(name = Fields.IS_ACTIVE)
    var isActive: Boolean = false

    @ColumnInfo(name = Fields.IS_DELETED)
    var isDeleted: Boolean = false

    /*@TypeConverters(DateConverter::class)
    @ColumnInfo(name = Fields.CREATE_AT)
    var createAt: Date? = null

    @TypeConverters(DateConverter::class)
    @ColumnInfo(name = Fields.UPDATE_AT)
    var updateAt: Date? = null*/


}