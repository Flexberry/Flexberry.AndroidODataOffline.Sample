package com.flexberry.androidodataofflinesample.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

private const val TABLE_NAME = "AppData"
@Entity(tableName = TABLE_NAME)
data class AppDataEntity(
    @PrimaryKey
    @ColumnInfo
    val primarykey : UUID,

    @ColumnInfo
    val isOnline: Boolean
) {
    companion object {
        const val tableName: String = TABLE_NAME
    }
}
